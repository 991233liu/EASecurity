package com.easecurity.admin.auth

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import com.easecurity.core.basis.b.User as BUser

@GrailsCompileStatic
@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    BUser buser
    String password = 'aa'
//    boolean enabled = true
//    boolean accountExpired
//    boolean accountLocked
//    boolean passwordExpired

    static hasMany = [coordinates: SecurityCoordinate]
    static transients = ['buser', 'password']

    Set<Role> getAuthorities() {
//        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        username nullable: false, blank: false, unique: true
//        password nullable: false, blank: false, password: true
//		enabled()
//		accountExpired()
//		accountLocked()
//		passwordExpired()
    }

    static mapping = {
//        table 'b_user_admin'
        table 'user_admin'
//	    password column: '`password`'
        version false
    }

    void setPassword(String password) {
        this.password = password
    }

    String getPassword() {
        return password
    }
}
