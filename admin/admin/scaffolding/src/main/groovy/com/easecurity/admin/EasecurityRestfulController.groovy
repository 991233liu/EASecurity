package com.easecurity.admin

import grails.artefact.Artefact
import grails.gorm.transactions.Transactional
import grails.rest.RestfulController

import com.alibaba.fastjson.JSON

import java.lang.reflect.Field

import static org.springframework.http.HttpStatus.OK

@Artefact("Controller")
class EasecurityRestfulController<T> extends RestfulController {

    EasecurityRestfulController(Class resource) {
        super(resource)
    }

    EasecurityRestfulController(Class resource, boolean readOnly) {
        super(resource, readOnly)
    }

    def index(Integer max) {
        if (max < 0) {
            max = null
        }
        params.max = Math.min(max ?: 10, 100)
        Map searchParams = [:]
        Boolean hasDeletedField = grailsApplication.getDomainClass(resource.name).hasProperty('deleted')
        if (params.search) {
            if (params.search instanceof String)
                searchParams = getSearchParams(JSON.parseObject(params.search, HashMap.class), resource, hasDeletedField)
            else
                searchParams = getSearchParams(params.search, resource, hasDeletedField)
        }
        String where = searchParams.remove('where')
        if (searchParams || hasDeletedField) {
            String orderBy = params.sort ? " order by ${params.sort} ${params.order}" : ""
            int count = resource.executeQuery("select count(id) from " + resourceClassName + " where" + where, searchParams)[0]
            List list = resource.findAll("from " + resourceClassName + " where" + where + orderBy, searchParams, params)
            respond list, model: [total: count, expand: expand, deep: params.deep ? params.deep : false]
        } else {
            respond listAllResources(params), model: [total: countResources(), expand: expand, deep: params.deep ? params.deep : false]
        }
    }

    /**
     * Shows a single resource
     * @param id The id of the resource
     * @return The rendered resource or a 404 if it doesn't exist
     */
    def show() {
        respond queryForResource(params.id), model: [expand: expand, deep: params.deep ? params.deep : false]
    }

    /**
     * Deletes a resource for the given ids
     * @param ids The ids
     */
    @Transactional
    def deleteAll() {
        if (handleReadOnly()) {
            return
        }

        Map data = request.getJSON()
        if (data)
            data.ids.each { it ->
                def instance = queryForResource(it)
                if (instance != null) deleteResource instance
//                if (it instanceof String) {
//                    def instance = queryForResource(it)
//                    if (instance != null) deleteResource instance
//                } else {
//                    def instance = queryForResource(Long.valueOf(it))
//                    if (instance != null) deleteResource instance
//                }
            }

        request.withFormat {
            '*' {
                render view: '/message', status: OK
            }
        }
    }

    /**
     * 拼装查询条件<p>
     *
     */
    private Map getSearchParams(Map<String, String> params, Class calzz, Boolean hasDeletedField = true) {
        if (params) {
            Map searchParams = [:]
            String where = ''
            params.each { String k, String v ->
                if (v != null && !''.equals(v)) {
                    Field field = null
                    try {
                        field = calzz.getField(k)
                    } catch (Exception e) {
                        field = calzz.getDeclaredField(k)
                    }
                    if (field.type == Integer) {
                        searchParams[k] = Integer.valueOf(v)
                        where += ' and ' + k + ' = :' + k
                    } else if (field.type == Boolean) {
                        searchParams[k] = Boolean.valueOf(v)
                        where += ' and ' + k + ' = :' + k
                    } else if (field.type.isEnum()) {
                        searchParams[k] = field.type.getMethod('getEnumByCode', String).invoke(null, v)
                        where += ' and ' + k + ' = :' + k
                    } else {
                        searchParams[k] = '%' + v + '%'
                        where += ' and ' + k + ' like :' + k
                    }
                }
            }
            if (hasDeletedField) where += ' and deleted = false'
            searchParams.where = where.replaceFirst('and', '')
            return searchParams
        } else {
            Map searchParams = [:]
            if (hasDeletedField) searchParams.where = ' deleted = false'
            return searchParams
        }
    }
}
