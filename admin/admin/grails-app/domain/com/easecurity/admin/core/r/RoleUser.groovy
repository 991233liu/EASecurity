package com.easecurity.admin.core.r

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(includes='id', includeNames=true, includePackage=false)
class RoleUser extends com.easecurity.core.basis.r.RoleUser {

	static constraints = {
//		id length:40
		usreid length:40, nullable: true
		roleid nullable: true
		user nullable: true
		roleCode nullable: true
	}

	static mapping = {
		table 'r_role_user'
		usreid index: 'IDX_USERID'
		roleid index: 'IDX_ROLEID'
		version false
	}
}
