package com.easecurity.admin.core.b

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class PostsServiceSpec extends Specification {

    PostsService postsService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Posts(...).save(flush: true, failOnError: true)
        //new Posts(...).save(flush: true, failOnError: true)
        //Posts posts = new Posts(...).save(flush: true, failOnError: true)
        //new Posts(...).save(flush: true, failOnError: true)
        //new Posts(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //posts.id
    }

    void "test get"() {
        setupData()

        expect:
        postsService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Posts> postsList = postsService.list(max: 2, offset: 2)

        then:
        postsList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        postsService.count() == 5
    }

    void "test delete"() {
        Long postsId = setupData()

        expect:
        postsService.count() == 5

        when:
        postsService.delete(postsId)
        sessionFactory.currentSession.flush()

        then:
        postsService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Posts posts = new Posts()
        postsService.save(posts)

        then:
        posts.id != null
    }
}
