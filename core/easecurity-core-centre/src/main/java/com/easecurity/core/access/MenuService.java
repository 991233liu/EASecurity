/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.MenuDo;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.au.MenuOrg;
import com.easecurity.core.basis.re.Menu;
import com.easecurity.core.redis.RedisUtil;
import com.easecurity.util.JsonUtils;

/**
 * 菜单访问控制服务。
 *
 */
@Service
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RedisUtil redisUtil;

    private static final Long MENU_TIMEOUT = (long) (10 * 60 * 60); // 10小时Redis缓存失效

    private static volatile Map<String, MenuDo> allMenuDoMap = null;
    private static volatile List<MenuDo> allMenuDoList = null;
    private static volatile List<MenuDo> rootMenuDoList = null; // 根list，支持多棵树
    // TODO 内存强制失效?
    private static volatile long validTime = -1;
    
    String sql = "SELECT * FROM re_menu order by sort_number";
    String sql2 = "SELECT * FROM au_menu_org where menuid = ?";
    String sql3 = "SELECT id FROM re_menu where parent_id = ? order by sort_number";

    /**
     * 初始化菜单信息，存入Redis
     */
    @SuppressWarnings("unchecked")
    public synchronized List<MenuDo> loadAll() {
	// 有效期内直接返回内存缓存的数据
	if (System.currentTimeMillis() < validTime)
	    return allMenuDoList;
	
	// 如果Redis中有缓存，则使用Redis中；如果没有，则加载数据库并缓存到Redis
	if (redisUtil.hasKey("menu:rootList")) {
	    allMenuDoMap = (Map<String, MenuDo>) redisUtil.get("menu:all");
	    allMenuDoList = allMenuDoMap.values().stream().collect(Collectors.toList());
	    rootMenuDoList = (List<MenuDo>) redisUtil.get("menu:rootList");
	} else {
	    Map<String, MenuDo> amdoAllMap = new HashMap<String, MenuDo>();
	    List<MenuDo> mdoAll = new ArrayList<>();
	    List<MenuDo> rootMdo = new ArrayList<>();
	    List<Menu> all = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Menu.class));
	    for (Menu menu : all) {
		// TODO 属性没加载全
		MenuDo mdo = new MenuDo();
		mdo.menu = menu;
		mdo.menuOrg = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(MenuOrg.class), menu.id);
		mdo.childMenuIds = jdbcTemplate.queryForList(sql3, String.class, menu.id);
		mdoAll.add(mdo);
		amdoAllMap.put("menu:" + menu.id, mdo);
		if (menu.parentId == null || menu.parentId < 1)
		    rootMdo.add(mdo);
		redisUtil.set("menu:" + menu.id, mdo, MENU_TIMEOUT);
	    }
	    redisUtil.set("menu:rootList", rootMdo, MENU_TIMEOUT);
	    redisUtil.set("menu:all", amdoAllMap, MENU_TIMEOUT);
	    allMenuDoMap = amdoAllMap;
	    allMenuDoList = mdoAll;
	    rootMenuDoList = rootMdo;
	}
	validTime = System.currentTimeMillis() + (5 * 60 * 1000); // 内存中5分钟内有效
	log.info("-------## 加载的MenuDo有：{}", allMenuDoList);
	return allMenuDoList;
    }

    @SuppressWarnings("unchecked")
    public List<MenuDo> getMenuByUser(UserDo user) {
	List<MenuDo> allMenuDo = new ArrayList<MenuDo>();
	Map<String, String> allIdentities = (Map<String, String>) JsonUtils.jsonToObject(user.allIdentities());
	// 遍历所有根菜单，并递归每个有权限的子菜单
	rootMenuDoList.forEach(item -> {
	    _getMenuDoByIdentities(allIdentities, item, allMenuDo);
	});
	return allMenuDo;
    }

    private void _getMenuDoByIdentities(Map<String, String> allIdentities, MenuDo menuDo, List<MenuDo> all) {
	allIdentities.forEach((k, v) -> {
	    if (_havePermission(k, v, menuDo)) {
		all.add(menuDo);
		List<MenuDo> child = getChildMenuDoByIdentities(allIdentities, menuDo);
		// 递归，遍历所有子节点
		child.forEach(item -> {
		    _getMenuDoByIdentities(allIdentities, item, all);
		});
	    }
	});
    }

    /**
     * 遍历权限，找到有权限的子菜单
     * 
     * @param allIdentities 权限集合
     * @param menuDo        父菜单
     * @return 有权限的子菜单列表
     */
    public List<MenuDo> getChildMenuDoByIdentities(Map<String, String> allIdentities, MenuDo menuDo) {
	List<MenuDo> childMenuDo = new ArrayList<MenuDo>();
	// 遍历子菜单，找到有权限的子菜单
	getChildMenuDo(menuDo).forEach(item -> {
	    allIdentities.forEach((k, v) -> {
		if (_havePermission(k, v, item))
		    childMenuDo.add(item);
	    });
	});
	return childMenuDo;
    }

    private boolean _havePermission(String identitiesType, String Identities, MenuDo menuDo) {
	// TODO 遍历权限，判定是否有菜单权限
	switch (identitiesType) {
	case "user":
	    break;
	case "org":
	    for (String id : Identities.split(",")) {
		if (menuDo.havePermissionThroughOrg(id)) {
		    return true;
		}
	    }
	    break;
	}
	return false;
    }

    /**
     * 获取子菜单列表
     * 
     * @param menuDo 父菜单
     * @return 子菜单列表
     */
    public List<MenuDo> getChildMenuDo(MenuDo menuDo) {
	List<MenuDo> chiildMenuDo = new ArrayList<>();
	menuDo.childMenuIds.forEach(id -> {
	    chiildMenuDo.add(allMenuDoMap.get("menu:" + id));
	});
	return chiildMenuDo;
    }
}
