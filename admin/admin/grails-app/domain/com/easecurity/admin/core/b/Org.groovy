package com.easecurity.admin.core.b

import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes = 'name,code', includeNames = true, includePackage = false)
class Org extends com.easecurity.core.basis.b.Org {

    static constraints = {
        name length:100, nullable: false, blank: false
        code nullable: true
        type length:2, nullable: true
        status length:2, nullable: true
        parentId nullable: true
        fullName length:4000, nullable: true
        fullPathid length:4000, nullable: true
        fullCode length:4000, nullable: true
    }

    static mapping = {
        table 'b_org'
        parentId index: 'IDX_PARENTID'
        version false
    }
}
