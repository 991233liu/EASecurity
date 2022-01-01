package com.easecurity.admin.core.r


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RoleUserController {

    RoleUserService roleUserService
    static public String VIEW_PATH = "/md/roleUser/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, RoleUser)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int roleUserCount = RoleUser.executeQuery("select count(id) from RoleUser where" + where, searchParams)[0]
            List roleUserList = RoleUser.findAll("from RoleUser where" + where + orderBy, searchParams, params)
            respond roleUserList, view: "${VIEW_PATH}index", model: [roleUserCount: roleUserCount]
        } else {
            respond roleUserService.list(params), view: "${VIEW_PATH}index", model:[roleUserCount: roleUserService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond roleUserService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new RoleUser(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(RoleUser roleUser) {
        if (roleUser == null) {
            notFound()
            return
        }

        try {
            roleUserService.save(roleUser)
        } catch (ValidationException e) {
            respond roleUser.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'roleUser.label', default: 'RoleUser'), roleUser.id])
                redirect roleUser
            }
            '*' { respond roleUser, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond roleUserService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(RoleUser roleUser) {
        if (roleUser == null) {
            notFound()
            return
        }

        try {
            roleUserService.save(roleUser)
        } catch (ValidationException e) {
            respond roleUser.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'roleUser.label', default: 'RoleUser'), roleUser.id])
                redirect roleUser
            }
            '*'{ respond roleUser, [status: OK] }
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
            roleUserService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'roleUser.label', default: 'RoleUser'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'roleUser.label', default: 'RoleUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
