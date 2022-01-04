<%=packageName ? "package ${packageName}" : ''%>

import com.easecurity.admin.utils.ServletUtils
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ${className}Controller {

//    static scaffold = ${className}

    ${className}Service ${propertyName}Service
    static public String VIEW_PATH = "/md/${propertyName}/"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

//    @Secured(["ROLE_USER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        if (params.search) {
            searchParams = ServletUtils.getSearchParams(params.search, ${className})
        }
        String where = searchParams.remove('where')
        if (searchParams) {
            String orderBy = params.sort ? " order by \${params.sort} \${params.order}" : ""
            int ${propertyName}Count = ${className}.executeQuery("select count(id) from ${className} where" + where, searchParams)[0]
            List ${propertyName}List = ${className}.findAll("from ${className} where" + where + orderBy, searchParams, params)
            respond ${propertyName}List, view: "\${VIEW_PATH}index", model: [${propertyName}Count: ${propertyName}Count]
        } else {
            respond ${propertyName}Service.list(params), view: "\${VIEW_PATH}index", model:[${propertyName}Count: ${propertyName}Service.count()]
        }
    }

//    @Secured(["ROLE_USER"])
    def show(Long id) {
        respond ${propertyName}Service.get(id), view: "\${VIEW_PATH}show"
    }

//    @Secured(["ROLE_USER"])
    def create() {
        respond new ${className}(params), view: "\${VIEW_PATH}create"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def save(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }

        try {
            ${propertyName}Service.save(${propertyName})
        } catch (ValidationException e) {
            respond ${propertyName}.errors, view: "\${VIEW_PATH}create"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: '${propertyName}.label', default: '${className}'), ${propertyName}.id])
                redirect ${propertyName}
            }
            '*' { respond ${propertyName}, [status: CREATED] }
        }
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def edit(Long id) {
        respond ${propertyName}Service.get(id), view: "\${VIEW_PATH}edit"
    }

//    @Secured(["ROLE_USER"])
    @Transactional
    def update(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }

        try {
            ${propertyName}Service.save(${propertyName})
        } catch (ValidationException e) {
            respond ${propertyName}.errors, view: "\${VIEW_PATH}edit"
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: '${propertyName}.label', default: '${className}'), ${propertyName}.id])
                redirect ${propertyName}
            }
            '*'{ respond ${propertyName}, [status: OK] }
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
            ${propertyName}Service.delete(Long.valueOf(it))
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: '${propertyName}.label', default: '${className}'), id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: '${propertyName}.label', default: '${className}'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
