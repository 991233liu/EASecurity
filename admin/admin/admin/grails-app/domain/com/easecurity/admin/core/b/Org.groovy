package com.easecurity.admin.core.b

import com.easecurity.admin.I18nSource
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes = 'name,code', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/b/Org.java")
class Org extends com.easecurity.core.basis.b.Org {

    Org parent

    static hasMany = [orgUsers: OrgUser, children: Org]

    static constraints = {
        name length: 100, nullable: false, blank: false
        code nullable: false
        type length: 4, nullable: true
        status length: 2, nullable: true
        parentId nullable: true
        fullName length: 4000, nullable: true
        fullPathid length: 4000, nullable: true
        fullCode length: 4000, nullable: true
        parent nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 'b_org'
        parentId index: 'IDX_PARENTID'
        type enumType: "identity"
        status enumType: "identity"
        parentId insertable: false
        parentId updateable: false
        version false
    }

    def beforeInsert() {
        if (parent) {
            fullName = parent.fullName + name + "/"
            fullCode = parent.fullCode + code + "/"
        } else {
            fullName = "/"
            fullPathid = "/"
            fullCode = "/"
        }
    }

    def beforeUpdate() {
        if (parent) {
            fullName = parent.fullName + name + "/"
            fullPathid = parent.fullPathid + id + "/"
            fullCode = parent.fullCode + code + "/"
        } else {
            fullName = "/"
            fullPathid = "/"
            fullCode = "/"
        }
    }

    def afterInsert() {
        if (parent) {
            Org.executeUpdate("update Org o set o.fullPathid = :fullPathid where o.id = :id", [fullPathid: parent.fullPathid + id + "/", id: id])
        }
    }
}
