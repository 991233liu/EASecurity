package com.easecurity.admin.core.b


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class OrgUserController {

    OrgUserService orgUserService
    static public String VIEW_PATH = "/md/orgUser/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, OrgUser)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int orgUserCount = OrgUser.executeQuery("select count(id) from OrgUser where" + where, searchParams)[0]
            List orgUserList = OrgUser.findAll("from OrgUser where" + where + orderBy, searchParams, params)
            respond orgUserList, view: "${VIEW_PATH}index", model: [orgUserCount: orgUserCount]
        } else {
            respond orgUserService.list(params), view: "${VIEW_PATH}index", model:[orgUserCount: orgUserService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond orgUserService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new OrgUser(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(OrgUser orgUser) {
        if (orgUser == null) {
            notFound()
            return
        }

        try {
            orgUserService.save(orgUser)
        } catch (ValidationException e) {
            respond orgUser.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'orgUser.label', default: 'OrgUser'), orgUser.id])
                redirect orgUser
            }
            '*' { respond orgUser, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond orgUserService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(OrgUser orgUser) {
        if (orgUser == null) {
            notFound()
            return
        }

        try {
            orgUserService.save(orgUser)
        } catch (ValidationException e) {
            respond orgUser.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'orgUser.label', default: 'OrgUser'), orgUser.id])
                redirect orgUser
            }
            '*'{ respond orgUser, [status: OK] }
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
            orgUserService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'orgUser.label', default: 'OrgUser'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'orgUser.label', default: 'OrgUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
