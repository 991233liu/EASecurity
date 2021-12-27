package com.easecurity.admin.core.b


import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class UserController {

    UserService userService
    static public String VIEW_PATH = "/md/user/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userService.list(params), view:"${VIEW_PATH}index", model:[userCount: userService.count()]
    }

//    @Secured(["ROLE_USER"])
    def show(String id) {
        respond userService.get(id), view:"${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new User(params), view:"${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userService.save(user)
        } catch (ValidationException e) {
            respond user.errors, view:"${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(String id) {
        respond userService.get(id), view:"${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userService.save(user)
        } catch (ValidationException e) {
            respond user.errors, view:"${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*'{ respond user, [status: OK] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def delete() {
        String id = params.id
        if (id == null) {
            notFound()
            return
        }

        String[] ids = [id]
        if (id.indexOf(",")) ids = id.split(",")
        else if (id.indexOf(";")) ids = id.split(";")

        ids.each { it ->
            userService.delete(it)
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
