package com.easecurity.admin.auth

import com.easecurity.admin.core.b.User
import com.easecurity.admin.core.r.RoleUser
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

/**
 * admin平台账号管理
 */
@GrailsCompileStatic
@EqualsAndHashCode(includes = 'account')
@ToString(includes = 'account', includeNames = true, includePackage = false)
class UserAdmin implements Serializable {

    private static final long serialVersionUID = 1

    String id
    /**
     * 账号
     */
    String account
    String password
    User user

    static transients = ['user', 'password']

    static constraints = {
        id length: 40
        account nullable: false, blank: false, unique: true
//        password nullable: false, blank: false, password: true
    }

    static mapping = {
        table 'b_user_admin'
        id generator: 'uuid'
        version false
    }

    def beforeInsert() {
        account = getUser()?.account
    }

    Set<String> getAuthorities() {
        Set<String> roles = []
        (RoleUser.findAllByAccount(this.account) as List<RoleUser>).each {
            roles.add('ROLE_' + it.roleCode)
        }
        roles
    }

    Set<String> getAuthoritiesGroup() {
        Set<String> roleGroups = []
        (RoleUser.findAllByAccount(this.account) as List<RoleUser>).each {
            roleGroups.add('ROLE_GROUP_' + it.role.roleGroup.code)
        }
        roleGroups.collect().unique().toSet()
    }

    String getPassword() {
        getUser()?.pd ?: ''
    }

    User getUser() {
        if (user == null) user = User.findByAccount(this.account)
        user
    }
}
