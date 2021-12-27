package com.easecurity.admin.core.r

import grails.gorm.services.Service

@Service(RoleUser)
interface RoleUserService {

    RoleUser get(Serializable id)

    List<RoleUser> list(Map args)

    Long count()

    void delete(Serializable id)

    RoleUser save(RoleUser roleUser)

}