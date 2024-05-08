package com.easecurity.admin.core.s

import com.easecurity.admin.core.re.Uri
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'lkey', includeNames = true, includePackage = false)
class Locker extends com.easecurity.core.basis.s.Locker {

    static constraints = {
	    id name: 'lkey'
        lockTime nullable: false
        timeout nullable: false
        timeoutTime nullable: false
    }

    static mapping = {
        table 's_locker'
		id generator: 'uuid'
        version false
    }
}
