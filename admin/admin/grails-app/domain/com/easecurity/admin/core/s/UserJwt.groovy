package com.easecurity.admin.core.s

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'id,account', includeNames = true, includePackage = false)
class UserJwt extends com.easecurity.core.basis.s.UserJwt {

    static constraints = {
        account nullable: false, blank: false
        jti length: 100, nullable: false, blank: false
        expires nullable: false
        jwt nullable: false
        dateCreated nullable: true
    }

    static mapping = {
        table 's_user_jwt'
		jwt type: 'text'
        version false
    }
}
