package com.easecurity.admin.core.b

import com.easecurity.admin.core.r.Role
import com.easecurity.admin.core.r.RoleGroup
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
        identities nullable: true
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
		identities type: 'text'
//	    password column: '`password`'
        version false
    }

    public static boolean updateIdentities(User user) {
        Map<String, HashMap> identities = [:]
        identities.user = [:]
        identities.user.id = user.id
        identities.user.account = user.account
        identities.user.name = user.userinfo?.name

        StringBuffer idBf = new StringBuffer("")
        StringBuffer codeBf = new StringBuffer("")
        StringBuffer nameBf = new StringBuffer("")
        StringBuffer fullCodeBf = new StringBuffer("")
        StringBuffer fullNameBf = new StringBuffer("")

        List<Posts> postses = []
        ((List<OrgUser>) OrgUser.findAllByUserId(user.id)).each { OrgUser orgUser ->
            idBf.append(",")
            idBf.append(orgUser.orgId)
            codeBf.append(",")
            codeBf.append(orgUser.org.code)
            nameBf.append(",")
            nameBf.append(orgUser.org.name)
            fullCodeBf.append(",")
            fullCodeBf.append(orgUser.org.fullCode)
            fullNameBf.append(",")
            fullNameBf.append(orgUser.org.fullName)
            if (!postses.contains(orgUser.posts)) postses.add(orgUser.posts)
        }
        if (!postses.isEmpty()) {
            identities.posts = [:]
            identities.posts.id = postses*.id.join(",")
            identities.posts.code = postses*.code.join(",")
            identities.posts.name = postses*.name.join(",")
        }
        if (idBf.length() > 1) {
            identities.org = [:]
            identities.org.id = idBf.substring(1)
            identities.org.code = codeBf.substring(1)
            identities.org.name = nameBf.substring(1)
            identities.org.fullCode = fullCodeBf.substring(1)
            identities.org.fullName = fullNameBf.substring(1)
        }

        idBf = new StringBuffer("")
        codeBf = new StringBuffer("")
        nameBf = new StringBuffer("")
        fullNameBf = new StringBuffer("")
        List<RoleGroup> roleGroups = []
        ((List<RoleUser>) RoleUser.findAllByUserId(user.id)).each { RoleUser roleUser ->
            idBf.append(",")
            idBf.append(roleUser.roleId)
            codeBf.append(",")
            codeBf.append(roleUser.roleCode)
            nameBf.append(",")
            nameBf.append(roleUser.role.name)
            fullNameBf.append(",")
            fullNameBf.append(roleUser.role.fullName)
            if (!roleGroups.contains(roleUser.role.roleGroup)) roleGroups.add(roleUser.role.roleGroup)
        }
		RoleGroup userRoleGroup = RoleGroup.findByCode("user")
		roleGroups.add(userRoleGroup)
		Role userRole = Role.findByRoleGroupId(userRoleGroup.id)
		idBf.append(",")
		idBf.append(userRole.id)
		codeBf.append(",")
		codeBf.append(userRole.code)
		nameBf.append(",")
		nameBf.append(userRole.name)
		fullNameBf.append(",")
		fullNameBf.append(userRole.fullName)
        if (!roleGroups.isEmpty()) {
            identities.roleGroup = [:]
            identities.roleGroup.id = roleGroups*.id.join(",")
            identities.roleGroup.code = roleGroups*.code.join(",")
            identities.roleGroup.name = roleGroups*.name.join(",")
        }
        if (idBf.length() > 1) {
            identities.role = [:]
            identities.role.id = idBf.substring(1)
            identities.role.code = codeBf.substring(1)
            identities.role.name = nameBf.substring(1)
            identities.role.fullName = fullNameBf.substring(1)
        }
        user.identities = JsonUtils.objectToJson(identities)
		System.out.println("-----## updateIdentities.identities=" + user.identities)
        user.save()
    }
}
