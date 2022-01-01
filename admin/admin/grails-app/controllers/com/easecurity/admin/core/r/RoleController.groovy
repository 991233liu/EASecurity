package com.easecurity.admin.core.r


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RoleController {

    RoleService roleService
    static public String VIEW_PATH = "/md/role/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, Role)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int roleCount = Role.executeQuery("select count(id) from Role where" + where, searchParams)[0]
            List roleList = Role.findAll("from Role where" + where + orderBy, searchParams, params)
            respond roleList, view: "${VIEW_PATH}index", model: [roleCount: roleCount]
        } else {
            respond roleService.list(params), view: "${VIEW_PATH}index", model:[roleCount: roleService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond roleService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new Role(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(Role role) {
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)
        } catch (ValidationException e) {
            respond role.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*' { respond role, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond roleService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(Role role) {
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)
        } catch (ValidationException e) {
            respond role.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*'{ respond role, [status: OK] }
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
        if ( id. indexOf ( "," ) ) ids = id.split ( "," )
        else if ( id.indexOf ( ";" ) ) ids = id.split ( ";" )

        ids.each { it ->
            roleService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label', default: 'Role'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label', default: 'Role'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
