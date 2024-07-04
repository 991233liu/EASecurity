package com.easecurity.admin.core.b

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.s.ApiSecret
import com.easecurity.admin.core.s.Certificates
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'sysid,name', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/b/InternalSystem.java")
class InternalSystem extends com.easecurity.core.basis.b.InternalSystem {

    static hasMany = [certificates: Certificates, apiSecrets: ApiSecret]

    static constraints = {
        certificates nullable: true
        apiSecrets nullable: true
        sysid length: 50, nullable: false, blank: false, unique: true
        name length: 100, nullable: false, blank: false
        cname length: 100, nullable: false, blank: false
        status length: 2, nullable: true
        memo length: 4000, nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_internal_system'
        status enumType: "identity"
        version false
    }
}
