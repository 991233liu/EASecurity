package com.easecurity.admin.core.b


import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class OrgController {

    OrgService orgService
    static public String VIEW_PATH = "/md/org/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond orgService.list(params), view:"${VIEW_PATH}index", model:[orgCount: orgService.count()]
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond orgService.get(id), view:"${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new Org(params), view:"${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(Org org) {
        if (org == null) {
            notFound()
            return
        }

        try {
            orgService.save(org)
        } catch (ValidationException e) {
            respond org.errors, view:"${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'org.label', default: 'Org'), org.id])
                redirect org
            }
            '*' { respond org, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond orgService.get(id), view:"${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(Org org) {
        if (org == null) {
            notFound()
            return
        }

        try {
            orgService.save(org)
        } catch (ValidationException e) {
            respond org.errors, view:"${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'org.label', default: 'Org'), org.id])
                redirect org
            }
            '*'{ respond org, [status: OK] }
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
            orgService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'org.label', default: 'Org'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'org.label', default: 'Org'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
