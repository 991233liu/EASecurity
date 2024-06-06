package com.easecurity.admin.core.re

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.au.UriIp
import com.easecurity.core.basis.re.MenuEnum
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'uri', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/re/Uri.java")
class Uri extends com.easecurity.core.basis.re.Uri {

    static hasMany = [uriIps: UriIp]

    static constraints = {
        uri length: 1000, nullable: false, blank: false
        classFullName length: 1000, nullable: true
        methodName nullable: true
        methodSignature length: 2000, nullable: true
        easType length: 2, nullable: true
        fromTo length: 2, nullable: true
        status length: 2, nullable: true
    }

    static mapping = {
        table 're_uri'
        easType enumType: "identity"
        fromTo enumType: "identity"
        status enumType: "identity"
        version false
    }

    def beforeInsert() {
//        if (parent) {
//            level = MenuEnum.Level.getAllEnum().get(parent.level.ordinal() + 1)
//            fullName = parent.fullName + name + "/"
//        } else {
//            level = MenuEnum.Level.ROOT
//            fullName = name + "/"
//        }
    }

    def beforeUpdate() {
//        if (parent) {
//            level = MenuEnum.Level.getAllEnum().get(parent.level.ordinal() + 1)
//            fullName = parent.fullName + name + "/"
//        } else {
//            level = MenuEnum.Level.ROOT
//            fullName = name + "/"
//        }
    }
}
