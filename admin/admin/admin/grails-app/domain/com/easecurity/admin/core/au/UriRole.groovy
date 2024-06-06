package com.easecurity.admin.core.au

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'uriId,roleId', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/au/UriRole.java")
class UriRole extends com.easecurity.core.basis.au.UriRole {

    Uri uri

//    static hasMany = [orgUsers: OrgUser, children: UriIp]

    static constraints = {
        uriId nullable: false, blank: false
        roleId nullable: true
		annotation length: 4000, nullable: true
        group1 nullable: true, range: 1..99
        fromTo length: 2, nullable: true
        status length: 2, nullable: true
    }

    static mapping = {
        table 'au_uri_role'
        fromTo enumType: "identity"
        status enumType: "identity"
        uriId insertable: false
        uriId updateable: false
        version false
    }

    def beforeInsert() {
    }

    def beforeUpdate() {
    }

    def afterInsert() {
    }
}
