package com.easecurity.admin.core.b

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name,account', includeNames = true, includePackage = false)
class UserInfo extends com.easecurity.core.basis.b.UserInfo {

    User user

    static constraints = {
        id length: 40
        userId length: 40, nullable: false, blank: false, unique: true
        account nullable: false, blank: false, unique: true
        name length: 50, nullable: true
        status nullable: true
        icon nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_user_info'
        id generator: 'uuid'
        status enumType: "ordinal"
        userId insertable: false
        userId updateable: false
        version false
    }

//    def beforeValidate() {
//        account = user.account
//    }

    def beforeInsert() {
        account = user.account
    }

    def beforeUpdate() {
        account = user.account
    }
}
