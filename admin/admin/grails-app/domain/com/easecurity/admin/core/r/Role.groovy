package com.easecurity.admin.core.r

import com.easecurity.admin.core.b.Org
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes = 'code', includeNames = true, includePackage = false)
class Role extends com.easecurity.core.basis.r.Role {

    Org org
    RoleGroup roleGroup

    static constraints = {
//		id length:40
        name nullable: true
        code nullable: true
        fullName nullable: true
        roleGroupId nullable: true, unique: 'orgId'
        orgId nullable: true
    }

    static mapping = {
        table 'r_role'
//        id generator: 'assigned'
        orgId insertable: false
        orgId updateable: false
        roleGroupId insertable: false
        roleGroupId updateable: false
        version false
    }

    def beforeValidate() {
//        if (id == null) id = org.id + "~" + roleGroup.id
    }

    def beforeInsert() {
//        if (id == null) id = org.id + "~" + roleGroup.id
        code = org.code + "#" + roleGroup.code
        fullName = org.name + roleGroup.name
    }

    def beforeUpdate() {
        code = org.code + "#" + roleGroup.code
        fullName = org.name + roleGroup.name
    }
}
