/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authorization;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.MenuDo;
import com.easecurity.core.basis.au.MenuOrg;
import com.easecurity.core.basis.re.Menu;

/**
 * 菜单授权服务。
 *
 */
@Service
public class MenuService {
    
    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 初始化菜单信息，存入Redis
     */
    public List<MenuDo> loadAll() {
	List<MenuDo> mdoAll = new ArrayList<>();
	List<Menu> all = jdbcTemplate.query("SELECT * FROM re_menu", new BeanPropertyRowMapper<>(Menu.class));
	for (Menu menu : all) {
	    MenuDo mdo = new MenuDo();
	    mdo.menu = menu;
	    mdo.menuOrg = jdbcTemplate.query("SELECT * FROM au_menu_org where menuid = ?", new BeanPropertyRowMapper<>(MenuOrg.class), menu.id);
	    mdoAll.add(mdo);
	}
	log.info("-------## 加载的MenuDo有：{}", mdoAll);
	return mdoAll;
    }
}
