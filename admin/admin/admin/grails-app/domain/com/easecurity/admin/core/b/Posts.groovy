package com.easecurity.admin.core.b

import com.easecurity.admin.I18nSource
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name,code', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/b/Posts.java")
class Posts extends com.easecurity.core.basis.b.Posts {

    static hasMany = [orgUsers: OrgUser]

    static constraints = {
        name nullable: false
        code nullable: false
        ranking nullable: true, range: 1..99
        type length: 2, nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_posts'
        type enumType: "identity"
        version false
    }
}
