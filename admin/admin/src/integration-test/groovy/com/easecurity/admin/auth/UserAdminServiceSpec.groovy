package com.easecurity.admin.auth

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserAdminServiceSpec extends Specification {

    UserAdminService userAdminService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new UserAdmin(...).save(flush: true, failOnError: true)
        //new UserAdmin(...).save(flush: true, failOnError: true)
        //UserAdmin userAdmin = new UserAdmin(...).save(flush: true, failOnError: true)
        //new UserAdmin(...).save(flush: true, failOnError: true)
        //new UserAdmin(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //userAdmin.id
    }

    void "test get"() {
        setupData()

        expect:
        userAdminService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<UserAdmin> userAdminList = userAdminService.list(max: 2, offset: 2)

        then:
        userAdminList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        userAdminService.count() == 5
    }

    void "test delete"() {
        Long userAdminId = setupData()

        expect:
        userAdminService.count() == 5

        when:
        userAdminService.delete(userAdminId)
        sessionFactory.currentSession.flush()

        then:
        userAdminService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        UserAdmin userAdmin = new UserAdmin()
        userAdminService.save(userAdmin)

        then:
        userAdmin.id != null
    }
}
