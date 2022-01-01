package com.easecurity.admin.auth


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class UserAdminController {

    UserAdminService userAdminService
    static public String VIEW_PATH = "/md/userAdmin/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, UserAdmin)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int userAdminCount = UserAdmin.executeQuery("select count(id) from UserAdmin where" + where, searchParams)[0]
            List userAdminList = UserAdmin.findAll("from UserAdmin where" + where + orderBy, searchParams, params)
            respond userAdminList, view: "${VIEW_PATH}index", model: [userAdminCount: userAdminCount]
        } else {
            respond userAdminService.list(params), view: "${VIEW_PATH}index", model:[userAdminCount: userAdminService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(String id) {
        respond userAdminService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new UserAdmin(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(UserAdmin userAdmin) {
        if (userAdmin == null) {
            notFound()
            return
        }

        try {
            userAdminService.save(userAdmin)
        } catch (ValidationException e) {
            respond userAdmin.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userAdmin.label', default: 'UserAdmin'), userAdmin.id])
                redirect userAdmin
            }
            '*' { respond userAdmin, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(String id) {
        respond userAdminService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(UserAdmin userAdmin) {
        if (userAdmin == null) {
            notFound()
            return
        }

        try {
            userAdminService.save(userAdmin)
        } catch (ValidationException e) {
            respond userAdmin.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'userAdmin.label', default: 'UserAdmin'), userAdmin.id])
                redirect userAdmin
            }
            '*'{ respond userAdmin, [status: OK] }
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
            userAdminService.delete(it)
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'userAdmin.label', default: 'UserAdmin'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userAdmin.label', default: 'UserAdmin'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
