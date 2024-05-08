package com.easecurity.admin.core.s

import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'id,account', includeNames = true, includePackage = false)
class UserToken extends com.easecurity.core.basis.s.UserToken {

    static constraints = {
        account nullable: false, blank: false
        accessToken length: 100, nullable: false, blank: false
        accessTokenExpires nullable: false
        refreshToken length: 100, nullable: false, blank: false
        refreshTokenExpires nullable: false
        userDetails nullable: false
        jwt nullable: false
        dateCreated nullable: true
    }

    static mapping = {
        table 's_user_token'
		userDetails type: 'text'
		jwt type: 'text'
        version false
    }
}
