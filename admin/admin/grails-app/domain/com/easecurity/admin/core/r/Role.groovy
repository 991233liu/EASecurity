package com.easecurity.admin.core.r

import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes='code', includeNames=true, includePackage=false)
class Role extends com.easecurity.core.basis.r.Role {

	static constraints = {
//		id length:40
		name nullable: true
		code nullable: true
		fullName nullable: true
		roleGroupid nullable: true
		orgid nullable: true
	}

	static mapping = {
		table 'r_role'
		id generator:'assigned'
		version false
	}
}
