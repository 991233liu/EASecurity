package com.easecurity.admin.core.b

import com.easecurity.admin.core.r.RoleUser
import com.easecurity.util.JsonUtils
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes = 'account', includeNames = true, includePackage = false)
class User extends com.easecurity.core.basis.b.User {

    static hasOne = [userinfo: UserInfo]
    static hasMany = [roleUsers: RoleUser]

    static constraints = {
        id length: 40
        account nullable: false, blank: false, unique: true
        pd length: 100, nullable: true
        acStatus nullable: true
        pdStatus nullable: true
        identities length: 4000, nullable: true
        userinfo nullable: true
        lastLoginTtime nullable: true
        pdErrorTimes nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_user'
        id generator: 'uuid'
        acStatus enumType: "ordinal"
        pdStatus enumType: "ordinal"
//	    password column: '`password`'
        version false
    }

    public static boolean updateIdentities(User user) {
        Map<String, String> identities = [user: user.id]
        StringBuffer bf = new StringBuffer("")
        List<Integer> postsIds = []
        ((List<OrgUser>) OrgUser.findAllByUserId(user.id)).each { OrgUser orgUser ->
            bf.append(",")
            bf.append(orgUser.orgId)
            if (!postsIds.contains(orgUser.postsId)) postsIds.add(orgUser.postsId)
        }
        if (!postsIds.isEmpty()) identities.posts = postsIds.join(",")
        if (bf.length() > 1) identities.org = bf.substring(1)
        bf = new StringBuffer("")
        List<Integer> roleGroupIds = []
        ((List<RoleUser>) RoleUser.findAllByUserId(user.id)).each { RoleUser roleUser ->
            bf.append(",")
            bf.append(roleUser.roleId)
            if (!roleGroupIds.contains(roleUser.role.roleGroupId)) roleGroupIds.add(roleUser.role.roleGroupId)
        }
        if (!roleGroupIds.isEmpty()) identities.roleGroup = roleGroupIds.join(",")
        if (bf.length() > 1) identities.role = bf.substring(1)
        user.identities = JsonUtils.objectToJson(identities)
        user.save()
    }
}
