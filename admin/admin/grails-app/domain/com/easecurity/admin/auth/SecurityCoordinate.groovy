package com.easecurity.admin.auth

import groovy.transform.CompileStatic

@CompileStatic
class SecurityCoordinate {
    String position
    String value
    static belongsTo = [user: UserAdmin]
    static transients = ['user']
}