package com.easecurity.admin.core.s

import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'hid,projetName', includeNames = true, includePackage = false)
class ServerHistory extends com.easecurity.core.basis.s.Server {

	Integer hid;
	Integer serverId;

    static constraints = {
		sid nullable: false
        projetName nullable: true
        name nullable: true
        startTime nullable: true
        ip nullable: true
        port nullable: true
        other nullable: true
        lastTime nullable: false
    }

    static mapping = {
        table 's_server_history'
		id name: 'hid'
		serverId column: 'id'
		other type: 'text'
		version false
    }
}
