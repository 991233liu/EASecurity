package com.easecurity

import grails.util.Environment

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        if (Environment.isDevelopmentMode()) {
            "/"(view: "/index")
        } else {
            "/"(view: "/home/index")
        }
        "403"(view: '/error/forbidden')
        "404"(view: '/error/notFound')
        "500"(view: '/error/error')
    }
}
