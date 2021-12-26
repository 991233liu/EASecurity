package com.easecurity.admin.core.b

import com.easecurity.admin.core.r.RoleUser
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes = 'account', includeNames = true, includePackage = false)
class User extends com.easecurity.core.basis.b.User {

    static hasOne = [userinfo: UserInfo]
    static hasMany = [roleUsers: RoleUser]

    static constraints = {
        id length: 40
        account nullable: false, blank: false, unique: true
        pd length: 100, nullable: true
        uStatus length: 2, nullable: true
        pStatus length: 2, nullable: true
        identities length: 4000, nullable: true
        userinfo nullable: true
    }

    static mapping = {
        table 'b_user'
        id generator: 'assigned'
//        account index: 'IDX_ACCOUNT'
//	    password column: '`password`'
        version false
    }

    def beforeValidate() {
        if (id == null) id = UUID.randomUUID().toString().replaceAll("-", "")
    }

    def beforeInsert() {
        if (id == null) id = UUID.randomUUID().toString().replaceAll("-", "")
    }
}
