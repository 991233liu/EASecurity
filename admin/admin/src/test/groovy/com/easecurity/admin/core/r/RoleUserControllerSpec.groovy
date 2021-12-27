package com.easecurity.admin.core.r

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class RoleUserControllerSpec extends Specification implements ControllerUnitTest<RoleUserController>, DomainUnitTest<RoleUser> {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        assert false, "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }

    void "Test the index action returns the correct model"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.roleUserList
        model.roleUserCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.roleUser!= null
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/roleUser/index'
        flash.message != null
    }

    void "Test the save action correctly persists"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * save(_ as RoleUser)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def roleUser = new RoleUser(params)
        roleUser.id = 1

        controller.save(roleUser)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/roleUser/show/1'
        controller.flash.message != null
    }

    void "Test the save action with an invalid instance"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * save(_ as RoleUser) >> { RoleUser roleUser ->
                throw new ValidationException("Invalid instance", roleUser.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def roleUser = new RoleUser()
        controller.save(roleUser)

        then:"The create view is rendered again with the correct model"
        model.roleUser != null
        view == 'create'
    }

    void "Test the show action with a null id"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * get(2) >> new RoleUser()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.roleUser instanceof RoleUser
    }

    void "Test the edit action with a null id"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * get(2) >> new RoleUser()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.roleUser instanceof RoleUser
    }


    void "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/roleUser/index'
        flash.message != null
    }

    void "Test the update action correctly persists"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * save(_ as RoleUser)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def roleUser = new RoleUser(params)
        roleUser.id = 1

        controller.update(roleUser)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/roleUser/show/1'
        controller.flash.message != null
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * save(_ as RoleUser) >> { RoleUser roleUser ->
                throw new ValidationException("Invalid instance", roleUser.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new RoleUser())

        then:"The edit view is rendered again with the correct model"
        model.roleUser != null
        view == 'edit'
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/roleUser/index'
        flash.message != null
    }

    void "Test the delete action with an instance"() {
        given:
        controller.roleUserService = Mock(RoleUserService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/roleUser/index'
        flash.message != null
    }
}






