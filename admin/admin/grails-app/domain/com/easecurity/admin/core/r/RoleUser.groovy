package com.easecurity.admin.core.r

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes='id', includeNames=true, includePackage=false)
class RoleUser extends com.easecurity.core.basis.r.RoleUser {

	static constraints = {
//		id length:40
		usreid length:40, nullable: true
		roleId nullable: true
		user nullable: true
		roleCode nullable: true
	}

	static mapping = {
		table 'r_role_user'
		usreid index: 'IDX_USERID'
		roleId index: 'IDX_ROLEID'
		version false
	}

	Role getRole() {
		Role.findById(this.roleId)
	}
}
