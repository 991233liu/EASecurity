package com.easecurity.admin.auth

import grails.gorm.services.Service

@Service(UserAdmin)
interface UserAdminService {

    UserAdmin get(Serializable id)

    List<UserAdmin> list(Map args)

    Long count()

    void delete(Serializable id)

    UserAdmin save(UserAdmin userAdmin)

}