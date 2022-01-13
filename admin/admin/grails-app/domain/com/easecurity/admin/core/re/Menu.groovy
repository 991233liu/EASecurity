package com.easecurity.admin.core.re

import com.easecurity.core.basis.re.MenuEnum
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name', includeNames = true, includePackage = false)
class Menu extends com.easecurity.core.basis.re.Menu {

    Menu parent

    static hasMany = [children: Menu]

    static constraints = {
        name nullable: false, blank: false
        code nullable: true
        level nullable: true
        sortNumber nullable: true
        parentId nullable: true
        openUrl length: 2000, nullable: true
        icon length: 2000, nullable: true
        fullName length: 4000, nullable: true
        status nullable: true
        displayStatus nullable: true
        disablePrompt length: 2000, nullable: true
        noPermissionsPrompt length: 2000, nullable: true
        accessType nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 're_menu'
        parentId index: 'IDX_PARENTID'
        level enumType: "ordinal"
        status enumType: "ordinal"
        displayStatus enumType: "ordinal"
        accessType enumType: "ordinal"
        parentId insertable: false
        parentId updateable: false
        version false
    }

    def beforeInsert() {
        if (parent) {
            level = MenuEnum.Level.getAllEnum().get(parent.level.ordinal() + 1)
            fullName = parent.fullName + name + "/"
        } else {
            level = MenuEnum.Level.ROOT
            fullName = name + "/"
        }
    }

    def beforeUpdate() {
        if (parent) {
            level = MenuEnum.Level.getAllEnum().get(parent.level.ordinal() + 1)
            fullName = parent.fullName + name + "/"
        } else {
            level = MenuEnum.Level.ROOT
            fullName = name + "/"
        }
    }
}
