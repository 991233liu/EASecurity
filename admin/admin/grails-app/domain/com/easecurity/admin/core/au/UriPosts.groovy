package com.easecurity.admin.core.au


import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'uriId,postsId', includeNames = true, includePackage = false)
class UriPosts extends com.easecurity.core.basis.au.UriPosts {

    Uri uri

//    static hasMany = [orgUsers: OrgUser, children: UriIp]

    static constraints = {
        uriId nullable: false, blank: false
        postsId nullable: true
		annotation length: 4000, nullable: true
        group1 nullable: true, range: 1..99
        fromTo nullable: true
        status nullable: true
    }

    static mapping = {
        table 'au_uri_posts'
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
