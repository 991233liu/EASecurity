package com.easecurity.admin.core.re

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class MenuServiceSpec extends Specification {

    MenuService menuService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Menu(...).save(flush: true, failOnError: true)
        //new Menu(...).save(flush: true, failOnError: true)
        //Menu menu = new Menu(...).save(flush: true, failOnError: true)
        //new Menu(...).save(flush: true, failOnError: true)
        //new Menu(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //menu.id
    }

    void "test get"() {
        setupData()

        expect:
        menuService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Menu> menuList = menuService.list(max: 2, offset: 2)

        then:
        menuList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        menuService.count() == 5
    }

    void "test delete"() {
        Long menuId = setupData()

        expect:
        menuService.count() == 5

        when:
        menuService.delete(menuId)
        sessionFactory.currentSession.flush()

        then:
        menuService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Menu menu = new Menu()
        menuService.save(menu)

        then:
        menu.id != null
    }
}
