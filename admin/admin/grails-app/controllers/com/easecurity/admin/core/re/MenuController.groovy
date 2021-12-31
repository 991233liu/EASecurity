package com.easecurity.admin.core.re


import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class MenuController {

    MenuService menuService
    static public String VIEW_PATH = "/md/menu/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, Menu)
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int menuCount = Menu.executeQuery("select count(id) from Menu where" + where, searchParams)[0]
            List menuList = Menu.findAll("from Menu where" + where + orderBy, searchParams, params)
            respond menuList, view: "${VIEW_PATH}index", model: [menuCount: menuCount]
        } else {
            respond menuService.list(params), view: "${VIEW_PATH}index", model:[menuCount: menuService.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond menuService.get(id), view: "${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new Menu(params), view: "${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(Menu menu) {
        if (menu == null) {
            notFound()
            return
        }

        try {
            menuService.save(menu)
        } catch (ValidationException e) {
            respond menu.errors, view: "${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'menu.label', default: 'Menu'), menu.id])
                redirect menu
            }
            '*' { respond menu, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond menuService.get(id), view: "${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(Menu menu) {
        if (menu == null) {
            notFound()
            return
        }

        try {
            menuService.save(menu)
        } catch (ValidationException e) {
            respond menu.errors, view: "${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'menu.label', default: 'Menu'), menu.id])
                redirect menu
            }
            '*'{ respond menu, [status: OK] }
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
            menuService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
