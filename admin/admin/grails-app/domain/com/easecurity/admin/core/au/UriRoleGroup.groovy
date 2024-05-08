package com.easecurity.admin.core.au


import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'uriId,roleGroupId', includeNames = true, includePackage = false)
class UriRoleGroup extends com.easecurity.core.basis.au.UriRoleGroup {

    Uri uri

//    static hasMany = [orgUsers: OrgUser, children: UriIp]

    static constraints = {
        uriId nullable: false, blank: false
        roleGroupId nullable: true
		annotation length: 4000, nullable: true
        group1 nullable: true, range: 1..99
        fromTo nullable: true
        status nullable: true
    }

    static mapping = {
        table 'au_uri_role_group'
        fromTo enumType: "ordinal"
        status enumType: "ordinal"
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
