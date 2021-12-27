package com.easecurity.admin.core.b

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserInfoServiceSpec extends Specification {

    UserInfoService userInfoService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new UserInfo(...).save(flush: true, failOnError: true)
        //new UserInfo(...).save(flush: true, failOnError: true)
        //UserInfo userInfo = new UserInfo(...).save(flush: true, failOnError: true)
        //new UserInfo(...).save(flush: true, failOnError: true)
        //new UserInfo(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //userInfo.id
    }

    void "test get"() {
        setupData()

        expect:
        userInfoService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<UserInfo> userInfoList = userInfoService.list(max: 2, offset: 2)

        then:
        userInfoList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        userInfoService.count() == 5
    }

    void "test delete"() {
        Long userInfoId = setupData()

        expect:
        userInfoService.count() == 5

        when:
        userInfoService.delete(userInfoId)
        sessionFactory.currentSession.flush()

        then:
        userInfoService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        UserInfo userInfo = new UserInfo()
        userInfoService.save(userInfo)

        then:
        userInfo.id != null
    }
}
