package com.easecurity.admin.auth

import groovy.transform.CompileStatic

@CompileStatic
interface CoordinateValidator {

    boolean isValidValueForPositionAndUserName(String value, String position, String username)
}
