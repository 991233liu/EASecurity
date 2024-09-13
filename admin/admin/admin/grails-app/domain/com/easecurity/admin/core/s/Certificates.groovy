package com.easecurity.admin.core.s

import com.easecurity.admin.I18nSource
import com.easecurity.admin.core.b.Channel
import com.easecurity.admin.core.b.InternalSystem
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'id', includeNames = true, includePackage = false)
@I18nSource("d:/work/code/oschina/EASecurity/develop/core/easecurity-core-api/src/main/java/com/easecurity/core/basis/s/Certificates.java")
class Certificates extends com.easecurity.core.basis.s.Certificates {

    static belongsTo = [channel: Channel, internalSystem: InternalSystem]

    static constraints = {
        channel nullable: true
        internalSystem nullable: true
        channelId nullable: true
        internalSystemId nullable: true
        keyId length: 100, nullable: false, blank: false
        publicKey length: 4000, nullable: false, blank: false
        privateKey length: 4000, nullable: false, blank: false
		options length: 4000, nullable: true
        type length: 3, nullable: false, blank: false
        status length: 2, nullable: false, blank: false
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        table 's_certificates'
        id generator: 'com.easecurity.admin.core.UUID8IdentifierGenerator'
        type enumType: "identity"
        status enumType: "identity"
        channelId insertable: false
        channelId updateable: false
        internalSystemId insertable: false
        internalSystemId updateable: false
        version false
    }
}
