package com.easecurity.admin.core.b


import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class PostsController {

    PostsService postsService
    static public String VIEW_PATH = "/md/posts/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond postsService.list(params), view:"${VIEW_PATH}index", model:[postsCount: postsService.count()]
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond postsService.get(id), view:"${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new Posts(params), view:"${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(Posts posts) {
        if (posts == null) {
            notFound()
            return
        }

        try {
            postsService.save(posts)
        } catch (ValidationException e) {
            respond posts.errors, view:"${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'posts.label', default: 'Posts'), posts.id])
                redirect posts
            }
            '*' { respond posts, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond postsService.get(id), view:"${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(Posts posts) {
        if (posts == null) {
            notFound()
            return
        }

        try {
            postsService.save(posts)
        } catch (ValidationException e) {
            respond posts.errors, view:"${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'posts.label', default: 'Posts'), posts.id])
                redirect posts
            }
            '*'{ respond posts, [status: OK] }
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
            postsService.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'posts.label', default: 'Posts'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'posts.label', default: 'Posts'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
