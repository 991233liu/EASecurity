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
        acStatus nullable: true
        pdStatus nullable: true
        identities length: 4000, nullable: true
        userinfo nullable: true
        lastLoginTtime nullable: true
        pdErrorTimes nullable: true
    }

    static mapping = {
        table 'b_user'
        id generator: 'uuid'
        acStatus enumType: "ordinal"
        pdStatus enumType: "ordinal"
//	    password column: '`password`'
        version false
    }
}
