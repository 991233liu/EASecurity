package com.easecurity.admin.core.b

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name,code', includeNames = true, includePackage = false)
class Posts extends com.easecurity.core.basis.b.Posts {

    static constraints = {
        name nullable: true
        code nullable: true
        ranking nullable: true
        type nullable: true
    }

    static mapping = {
        table 'b_posts'
        version false
    }
}
