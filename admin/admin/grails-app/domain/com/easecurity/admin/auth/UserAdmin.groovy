package com.easecurity.admin.auth

import com.easecurity.admin.core.b.User
import com.easecurity.admin.core.r.RoleUser
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes = 'account')
@ToString(includes = 'account', includeNames = true, includePackage = false)
class UserAdmin implements Serializable {

    private static final long serialVersionUID = 1

    String id
    String account
    String password
    User buser
//    boolean enabled = true
//    boolean accountExpired
//    boolean accountLocked
//    boolean passwordExpired

    static hasMany = [coordinates: SecurityCoordinate]
    static transients = ['buser', 'password', 'coordinates']

    static constraints = {
        id length: 40
        account nullable: false, blank: false, unique: true
//        password nullable: false, blank: false, password: true
    }

    Set<String> getAuthorities() {
        Set<String> roles = []
        (RoleUser.findAllByUserId(this.id) as List<RoleUser>).each {
            /*
             * Role names must start with “ROLE_”. This is configurable in Spring Security, but not in the plugin. It would be possible to allow different prefixes,
             * but it’s important that the prefix not be blank as the prefix is used to differentiate between role names and tokens such as IS_AUTHENTICATED_FULLY/IS_AUTHENTICATED_ANONYMOUSLY/etc., and SpEL expressions.
             * The role names should be primarily an internal implementation detail; if you want to display friendlier names in a UI, it’s simple to remove the prefix first.
             */
            roles.add('ROLE_' + it.roleCode)
        }
        roles
//        (RoleUser.findAllByUsreid(this.id) as List<RoleUser>)*.roleCode as Set<String>
    }

    static mapping = {
        table 'b_user_admin'
        id generator: 'assigned'
//	    password column: '`password`'
        version false
    }

    void setPassword(String password) {
        this.password = password
    }

    String getPassword() {
        User.findByAccount(this.account)?.pd ?: ''
//        def dataSource = BeanUtils.getBean('dataSource');
//        def sql = new sql(dataSource);
//        def sql = Sql.newInstance()
//        String pd=""
//        sql.eachRow("SELECT pd FROM b_user WHERE user = :user",[user: this.username]){
//            pd=it.first()
//        }
//        User.executeQuery("SELECT pd FROM b_user WHERE user = :user",[user: this.username]).first()
    }
}
