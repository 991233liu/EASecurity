package com.easecurity.admin.core.r

import com.easecurity.admin.core.b.Org
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'code', includeNames = true, includePackage = false)
class RoleGroup extends com.easecurity.core.basis.r.RoleGroup {

    static hasMany = [roles: Role]

    static constraints = {
        name nullable: true
        code nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'r_role_group'
        version false
    }
}
