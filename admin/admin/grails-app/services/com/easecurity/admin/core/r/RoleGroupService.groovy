package com.easecurity.admin.core.r

import grails.gorm.services.Service

@Service(RoleGroup)
interface RoleGroupService {

    RoleGroup get(Serializable id)

    List<RoleGroup> list(Map args)

    Long count()

    void delete(Serializable id)

    RoleGroup save(RoleGroup roleGroup)

}