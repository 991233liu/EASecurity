package com.easecurity.admin

import grails.util.Environment

class UrlMappings {
    static mappings = {
        // rest模式下请求
        get "/$controller(.$format)?"(action: "index")
        get "/$controller/$id(.$format)?"(action: "show")
        post "/$controller(.$format)?"(action: "save")
        put "/$controller/$id(.$format)?"(action: "update")
        patch "/$controller/$id(.$format)?"(action: "patch")
        delete "/$controller/$id(.$format)?"(action: "delete")
        // 传统模式下请求
        "/$controller/$action/$id?(.$format)?"()
        if (Environment.isDevelopmentMode()) {
	        "/"(controller: 'application', action: 'index')
        } else {
	        "/"(controller: 'application', action: 'index')
        }
        "403"(view: '/errors/forbidden')
        "404"(view: '/errors/notFound')
        "500"(view: '/errors/error')
    }
}
