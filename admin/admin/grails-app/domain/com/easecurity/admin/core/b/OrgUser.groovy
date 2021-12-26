package com.easecurity.admin.core.b

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'account,orgId', includeNames = true, includePackage = false)
class OrgUser extends com.easecurity.core.basis.b.OrgUser {

    Org org
    User user
    Posts posts

    static constraints = {
        userId length: 40, nullable: false, blank: false
        account nullable: true
        orgId nullable: false, blank: false
        postsId nullable: true
        masterPosts nullable: true
    }

    static mapping = {
        table 'b_org_user'
        userId index: 'IDX_USERID'
        orgId index: 'IDX_ORGID'
        postsId index: 'IDX_POSTSID'
        userId insertable: false
        userId updateable: false
        orgId insertable: false
        orgId updateable: false
        postsId insertable: false
        postsId updateable: false
        version false
    }

    def beforeInsert() {
        account = user.account
    }

    def beforeUpdate() {
        account = user.account
    }
}
