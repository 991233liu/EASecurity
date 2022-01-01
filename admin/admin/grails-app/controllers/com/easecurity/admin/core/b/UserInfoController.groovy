package com.easecurity.admin.core.b


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class UserInfoController {

    UserInfoService userInfoService
    static public String VIEW_PATH = "/md/userInfo/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, UserInfo)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int userInfoCount = UserInfo.executeQuery("select count(id) from UserInfo where" + where, searchParams)[0]
            List userInfoList = UserInfo.findAll("from UserInfo where" + where + orderBy, searchParams, params)
            respond userInfoList, view: "${VIEW_PATH}index", model: [userInfoCount: userInfoCount]
        } else {
            respond userInfoService.list(params), view: "${VIEW_PATH}index", model:[userInfoCount: userInfoService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(String id) {
        respond userInfoService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new UserInfo(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(UserInfo userInfo) {
        if (userInfo == null) {
            notFound()
            return
        }

        try {
            userInfoService.save(userInfo)
        } catch (ValidationException e) {
            respond userInfo.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userInfo.label', default: 'UserInfo'), userInfo.id])
                redirect userInfo
            }
            '*' { respond userInfo, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(String id) {
        respond userInfoService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(UserInfo userInfo) {
        if (userInfo == null) {
            notFound()
            return
        }

        try {
            userInfoService.save(userInfo)
        } catch (ValidationException e) {
            respond userInfo.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'userInfo.label', default: 'UserInfo'), userInfo.id])
                redirect userInfo
            }
            '*'{ respond userInfo, [status: OK] }
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
            userInfoService.delete(it)
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'userInfo.label', default: 'UserInfo'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userInfo.label', default: 'UserInfo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
