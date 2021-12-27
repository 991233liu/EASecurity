package com.easecurity.admin.core.b

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class OrgUserServiceSpec extends Specification {

    OrgUserService orgUserService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new OrgUser(...).save(flush: true, failOnError: true)
        //new OrgUser(...).save(flush: true, failOnError: true)
        //OrgUser orgUser = new OrgUser(...).save(flush: true, failOnError: true)
        //new OrgUser(...).save(flush: true, failOnError: true)
        //new OrgUser(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //orgUser.id
    }

    void "test get"() {
        setupData()

        expect:
        orgUserService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<OrgUser> orgUserList = orgUserService.list(max: 2, offset: 2)

        then:
        orgUserList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        orgUserService.count() == 5
    }

    void "test delete"() {
        Long orgUserId = setupData()

        expect:
        orgUserService.count() == 5

        when:
        orgUserService.delete(orgUserId)
        sessionFactory.currentSession.flush()

        then:
        orgUserService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        OrgUser orgUser = new OrgUser()
        orgUserService.save(orgUser)

        then:
        orgUser.id != null
    }
}
