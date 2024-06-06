package com.easecurity.admin.core.b

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.*

class UserInfoControllerSpec extends Specification implements ControllerUnitTest<UserInfoController>, DomainUnitTest<UserInfo> {

    def populateValidParams(params) {
        assert params != null
        assert false, "TODO: Populate valid params"
    }

    def populateInvalidParams(params) {
        assert params != null
        assert false, "TODO: Populate invalid params"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index().get()

        then:"The model is correct"
            !model.userInfoList
            model.userInfoCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.userInfo!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.method = "POST"
            def userInfo= new UserInfo()
            userInfo.validate()
            controller.save(userInfo).get()

        then:"The create view is rendered again with the correct model"
            model.userInfo!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            userInfo= new UserInfo(params)

            controller.save(userInfo).get()

        then:"A redirect is issued to the show action"
            response.status == 201
            UserInfo.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is execu ted with a null domain"
            controller.show(null).get()

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def userInfo= new UserInfo(params).save(flush:true)

            controller.show(userInfo.id).get()

        then:"A model is populated containing the domain instance"
            model.userInfo.id==userInfo.id
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null).get()

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def userInfo = new UserInfo(params).save(flush:true)
            controller.edit(userInfo?.id).get()

        then:"A model is populated containing the domain instance"
            model.userInfo.id==userInfo.id
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.method = "PUT"
            controller.update(null).get()

        then:"A 404 error is returned"
            status == 404

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            def userInfo= new UserInfo(params).save(flush:true)
            params.clear()
            populateInvalidParams(params)
            controller.update(userInfo.id).get()

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.userInfo.id==userInfo.id

        when:"A valid domain instance is passed to the update action"
            response.reset()
            params.clear()
            populateValidParams(params)
            controller.update(userInfo.id).get()

        then:"A redirect is issued to the show action"
            userInfo != null
            response.status == 200
            !book.isDirty()
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.method = "DELETE"
            controller.delete(null).get()

        then:"A 404 is returned"
            status == 404

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def userInfo= new UserInfo(params).save(flush: true)

        then:"It exists"
            UserInfo.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(userInfo.id).get()

        then:"The instance is deleted"
            UserInfo.count() == 0
            response.status == 204
    }
}