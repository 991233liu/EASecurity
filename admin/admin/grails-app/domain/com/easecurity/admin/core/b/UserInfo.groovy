package com.easecurity.admin.core.b

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'user', includeNames = true, includePackage = false)
class UserInfo extends com.easecurity.core.basis.b.UserInfo {

    static constraints = {
        id length:40
        userId length:40, nullable: false, blank: false, unique: true
        user nullable: false, blank: false, unique: true
        name length:50, nullable: true
        status length:2, nullable: true
    }

    static mapping = {
        table 'b_user_info'
        id generator:'assigned'
        version false
    }
}
