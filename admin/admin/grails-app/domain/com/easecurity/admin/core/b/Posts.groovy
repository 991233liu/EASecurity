package com.easecurity.admin.core.b

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name,code', includeNames = true, includePackage = false)
class Posts extends com.easecurity.core.basis.b.Posts {

    static hasMany = [orgUsers: OrgUser]

    static constraints = {
        name nullable: false
        code nullable: false
        ranking nullable: true, range: 1..99
        type nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_posts'
        type enumType: "ordinal"
        version false
    }
}
