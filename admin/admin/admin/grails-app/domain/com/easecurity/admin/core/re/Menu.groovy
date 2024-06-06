package com.easecurity.admin.core.re

import com.easecurity.admin.I18nSource
import com.easecurity.core.basis.re.MenuEnum
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'name', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/re/Menu.java")
class Menu extends com.easecurity.core.basis.re.Menu {

    Menu parent

    static hasMany = [children: Menu]

    static constraints = {
        name nullable: false, blank: false
        code nullable: true
        level length: 4, nullable: true
        sortNumber nullable: true
        parentId nullable: true
        openUrl length: 2000, nullable: true
        icon length: 2000, nullable: true
        fullName length: 4000, nullable: true
        status length: 2, nullable: true
        displayStatus length: 2, nullable: true
        disablePrompt length: 2000, nullable: true
        noPermissionsPrompt length: 2000, nullable: true
        accessType length: 2, nullable: true
    }

    static mapping = {
        table 're_menu'
        parentId index: 'IDX_PARENTID'
        level enumType: "identity"
        status enumType: "identity"
        displayStatus enumType: "identity"
        accessType enumType: "identity"
        parentId insertable: false
        parentId updateable: false
        version false
    }

    def beforeInsert() {
        if (parent) {
            level = MenuEnum.Level.values()[parent.level.ordinal() + 1]
            fullName = parent.fullName + name + "/"
        } else {
            level = MenuEnum.Level.ROOT
            fullName = name + "/"
        }
    }

    def beforeUpdate() {
        if (parent) {
            level = MenuEnum.Level.values()[parent.level.ordinal() + 1]
            fullName = parent.fullName + name + "/"
        } else {
            level = MenuEnum.Level.ROOT
            fullName = name + "/"
        }
    }
}
