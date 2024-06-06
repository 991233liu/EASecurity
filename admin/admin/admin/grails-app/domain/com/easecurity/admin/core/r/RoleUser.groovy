package com.easecurity.admin.core.r

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.b.User
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'id,account,roleCode', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/r/RoleUser.java")
class RoleUser extends com.easecurity.core.basis.r.RoleUser {

    User user
    Role role

    static constraints = {
//		id length:40
        userId length: 40, nullable: true
        roleId nullable: true, unique: 'userId'
        account nullable: true
        roleCode nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'r_role_user'
//        userId index: 'IDX_USERID'
        roleId index: 'IDX_ROLEID'
        userId insertable: false
        userId updateable: false
        roleId insertable: false
        roleId updateable: false
        version false
    }

    def beforeInsert() {
        account = user?.account
        roleCode = role?.code
    }

    def beforeUpdate() {
        account = user?.account
        roleCode = role?.code
    }

}
