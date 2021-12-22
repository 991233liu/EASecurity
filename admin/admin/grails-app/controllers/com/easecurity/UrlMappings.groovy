package com.easecurity

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "403"(view:'/error/forbidden')
        "404"(view:'/error/notFound')
        "500"(view:'/error/error')
    }
}
