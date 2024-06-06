package com.easecurity.admin.core.r

class RoleController {
    static scaffold = Role
    static expand = ["roleUsers","org","roleGroup"]

}
