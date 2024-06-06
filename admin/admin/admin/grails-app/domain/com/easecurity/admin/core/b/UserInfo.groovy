package com.easecurity.admin.core.b

import com.easecurity.admin.I18nSource
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name,account', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/b/UserInfo.java")
class UserInfo extends com.easecurity.core.basis.b.UserInfo {

    User user

    static constraints = {
        id length: 40
        userId length: 40, nullable: false, blank: false, unique: true
        account nullable: false, blank: false, unique: true
        name length: 50, nullable: true
        status length: 2, nullable: true
        avatar nullable: true
        email nullable: true
        phone nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_user_info'
        id generator: 'uuid'
        status enumType: "identity"
        userId insertable: false
        userId updateable: false
        version false
    }

    def beforeInsert() {
        account = user.account
    }

    def beforeUpdate() {
        account = user.account
    }
}
