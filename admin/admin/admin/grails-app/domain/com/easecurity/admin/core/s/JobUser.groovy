package com.easecurity.admin.core.s

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'tabName,lastTime', includeNames = true, includePackage = false)
class JobUser {

    /*
    * 表名
    */
    String tabName
    /*
    * 上一次成功完成时间
    */
    Date lastTime

//    static hasOne = [userinfo: UserInfo]
//    static hasMany = [roleUsers: RoleUser]

    static constraints = {
    }

    static mapping = {
        table 's_job_user'
        version false
    }
}
