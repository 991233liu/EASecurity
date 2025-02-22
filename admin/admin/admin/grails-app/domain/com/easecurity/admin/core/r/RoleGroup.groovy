package com.easecurity.admin.core.r

import com.easecurity.admin.I18nSource
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'code', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/r/RoleGroup.java")
class RoleGroup extends com.easecurity.core.basis.r.RoleGroup {

    static hasMany = [roles: Role]

    static constraints = {
        name nullable: false
        code nullable: false
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'r_role_group'
        version false
    }
}
