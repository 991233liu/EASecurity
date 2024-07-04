/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.easecurity.core.db.BeanPropertyRowMapper;
import com.easecurity.core.utils.RedisUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.authentication.UserDetails;
import com.easecurity.core.basis.au.MenuOrg;
import com.easecurity.core.basis.re.Menu;
import com.easecurity.core.basis.re.MenuEnum.*;
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
    private static volatile long validTime = -1;

    String sql = "SELECT * FROM re_menu order by level, sort_number";
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
                if (menu.level == Level.ROOT || menu.parentId == null)
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

    /**
     * 制定人员的的菜单
     * 
     * @param user         用户
     * @param rootMenuCode 根菜单code
     * @return
     */
    public List<MenuVo> getMenuByUser(UserDo user, String rootMenuCode) {
        return getMenuByUserIdentities(user.allIdentities(), rootMenuCode);
    }

    /**
     * 制定人员的的菜单
     * 
     * @param user         用户
     * @param rootMenuCode 根菜单code
     * @return
     */
    public List<MenuVo> getMenuByUser(UserDetails user, String rootMenuCode) {
        return getMenuByUserIdentities(user.identities, rootMenuCode);
    }

    /**
     * 制定人员的的菜单
     * 
     * @param identities
     * @param rootMenuCode 根菜单code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MenuVo> getMenuByUserIdentities(String identities, String rootMenuCode) {
        List<MenuVo> allMenuDo = new ArrayList<MenuVo>();
        Map<String, Map<String, String>> allIdentities = (Map<String, Map<String, String>>) JsonUtils.jsonToObject(identities);
        if (rootMenuCode == null || "".equals(rootMenuCode)) {
            // 遍历所有根菜单，并递归每个有权限的子菜单
            rootMenuDoList.forEach(item -> {
                _getMenuDoByIdentities(allIdentities, item, allMenuDo);
            });
        } else {
            MenuDo root = null;
            for (MenuDo menuDo : rootMenuDoList) {
                if (rootMenuCode.equals(menuDo.menu.code)) {
                    root = menuDo;
                    break;
                }
            }
            if (root == null)
                return null;
            _getMenuDoByIdentities(allIdentities, root, allMenuDo);
        }
        return allMenuDo;
    }

    // TODO 有个bug，禁用隐藏和无权限隐藏的关系？？？？
    private void _getMenuDoByIdentities(Map<String, Map<String, String>> allIdentities, MenuDo menuDo, List<MenuVo> all) {
        // 判断菜单状态
        MenuVo menuVo = null;
        switch (menuDo.menu.displayStatus) {
        case DISPLAY: // 始终显示
            menuVo = new MenuVo(menuDo);
            all.add(menuVo);
            menuVo.hasPermission = _havePermission(allIdentities, menuDo);
            if (menuDo.menu.status == Status.ENABLED) {
                List<MenuDo> allChild = getChildMenuDo(menuDo);
                List<MenuVo> child = new ArrayList<>();
                // 递归，遍历所有子节点
                allChild.forEach(item -> {
                    _getMenuDoByIdentities(allIdentities, item, child);
                });
                if (!child.isEmpty()) {
                    menuVo.childMenu = child;
                    menuVo.childMenuIds = new ArrayList<>();
                    for (MenuVo menuVo2 : child) {
                        menuVo.childMenuIds.add(String.valueOf(menuVo2.id));
                    }
                }
            }
            break;
        case HIDDEN: // 始终隐藏
            return;
        case DISABLEDHIDDEN: // 禁用隐藏
            if (menuDo.menu.status == Status.DISABLED) {
                return;
            }
        case NOPERMISSIONSHIDDEN: // 无权限隐藏
        default:
            // 启用状态下，且有权限的菜单，可以显示
            if (menuDo.menu.status == Status.ENABLED && _havePermission(allIdentities, menuDo)) {
                menuVo = new MenuVo(menuDo);
                all.add(menuVo);
                List<MenuDo> allChild = getChildMenuDo(menuDo);
                List<MenuVo> child = new ArrayList<>();
                // 递归，遍历所有子节点
                allChild.forEach(item -> {
                    _getMenuDoByIdentities(allIdentities, item, child);
                });
                if (!child.isEmpty()) {
                    menuVo.childMenu = child;
                    menuVo.childMenuIds = new ArrayList<>();
                    for (MenuVo menuVo2 : child) {
                        menuVo.childMenuIds.add(String.valueOf(menuVo2.id));
                    }
                }
            }
            break;
        }
    }

    private boolean _havePermission(Map<String, Map<String, String>> allIdentities, MenuDo menuDo) {
        if (menuDo.menu.accessType == AccessType.ANONYMOUS) {
            return true;
        } else if (menuDo.menu.accessType == AccessType.LOGIN && allIdentities != null) {
            return true;
        } else {
            for (String k : allIdentities.keySet()) {
                if (_havePermission(k, allIdentities.get(k).get("id"), menuDo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean _havePermission(String identitiesType, String Identities, MenuDo menuDo) {
        // TODO 遍历权限，判定是否有菜单权限
        switch (identitiesType) {
        case "role":
            break;
        case "roleGroup":
            break;
        case "org":
            for (String id : Identities.split(",")) {
                if (menuDo.havePermissionByOrgId(id)) {
                    return true;
                }
            }
            break;
        case "posts":
            break;
        case "user":
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
