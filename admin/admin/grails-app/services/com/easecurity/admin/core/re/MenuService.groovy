package com.easecurity.admin.core.re

import grails.gorm.services.Service

@Service(Menu)
interface MenuService {

    Menu get(Serializable id)

    List<Menu> list(Map args)

    Long count()

    void delete(Serializable id)

    Menu save(Menu menu)

}