package com.easecurity.admin.core.s

import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'id,projetName', includeNames = true, includePackage = false)
class Server extends com.easecurity.core.basis.s.Server {

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
        table 's_server'
		other type: 'text'
        version false
//		tablePerHierarchy false
//		tablePerConcreteClass true
    }
}
