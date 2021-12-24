package com.easecurity.admin.auth

import com.easecurity.admin.core.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes = 'user')
@ToString(includes = 'user', includeNames = true, includePackage = false)
class UserAdmin implements Serializable {

    private static final long serialVersionUID = 1

    String id
    String user
    String password
    User buser
//    boolean enabled = true
//    boolean accountExpired
//    boolean accountLocked
//    boolean passwordExpired

    static hasMany = [coordinates: SecurityCoordinate]
    static transients = ['buser', 'password', 'coordinates']

    Set<Object> getAuthorities() {
//        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        id length:40
        user nullable: false, blank: false, unique: true
//        password nullable: false, blank: false, password: true
    }

    static mapping = {
        table 'b_user_admin'
        id generator:'assigned'
//	    password column: '`password`'
        version false
    }

    void setPassword(String password) {
        this.password = password
    }

    String getPassword() {
        User.findByUser(this.user)?.pd?:''
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
