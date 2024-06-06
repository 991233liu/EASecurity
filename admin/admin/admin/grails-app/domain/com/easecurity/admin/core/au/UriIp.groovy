package com.easecurity.admin.core.au

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.b.OrgUser
import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'uriId,ips', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/au/UriIp.java")
class UriIp extends com.easecurity.core.basis.au.UriIp {

    Uri uri

//    static hasMany = [orgUsers: OrgUser, children: UriIp]

    static constraints = {
        uriId nullable: false, blank: false
        ips length: 4000,nullable: true
        group1 nullable: true, range: 1..99
        fromTo length: 2, nullable: true
        status length: 2, nullable: true
    }

    static mapping = {
        table 'au_uri_ip'
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
