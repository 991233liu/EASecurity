package com.easecurity.admin.core.r


import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RoleGroupController {

    RoleGroupService roleGroupService
    static public String VIEW_PATH = "/md/roleGroup/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond roleGroupService.list(params), view:"${VIEW_PATH}index", model:[roleGroupCount: roleGroupService.count()]
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond roleGroupService.get(id), view:"${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new RoleGroup(params), view:"${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(RoleGroup roleGroup) {
        if (roleGroup == null) {
            notFound()
            return
        }

        try {
            roleGroupService.save(roleGroup)
        } catch (ValidationException e) {
            respond roleGroup.errors, view:"${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), roleGroup.id])
                redirect roleGroup
            }
            '*' { respond roleGroup, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond roleGroupService.get(id), view:"${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(RoleGroup roleGroup) {
        if (roleGroup == null) {
            notFound()
            return
        }

        try {
            roleGroupService.save(roleGroup)
        } catch (ValidationException e) {
            respond roleGroup.errors, view:"${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), roleGroup.id])
                redirect roleGroup
            }
            '*'{ respond roleGroup, [status: OK] }
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
            roleGroupService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'roleGroup.label', default: 'RoleGroup'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
