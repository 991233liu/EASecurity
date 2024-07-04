package com.easecurity.admin.core.s

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.b.Channel
import com.easecurity.admin.core.b.InternalSystem
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'skey', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/s/ApiSecret.java")
class ApiSecret extends com.easecurity.core.basis.s.ApiSecret {

    static belongsTo = [channel: Channel, internalSystem: InternalSystem]

    static constraints = {
        channel nullable: true
        internalSystem nullable: true
        channelId nullable: true
        internalSystemId nullable: true
        skey length: 100, nullable: false, blank: false
        secret length: 100, nullable: false, blank: false
        options length: 4000, nullable: true
        status length: 2, nullable: false, blank: false
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 's_api_secret'
        status enumType: "identity"
        channelId insertable: false
        channelId updateable: false
        internalSystemId insertable: false
        internalSystemId updateable: false
        version false
    }
}
