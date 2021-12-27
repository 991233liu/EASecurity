package com.easecurity.admin.core.b

import grails.gorm.services.Service

@Service(OrgUser)
interface OrgUserService {

    OrgUser get(Serializable id)

    List<OrgUser> list(Map args)

    Long count()

    void delete(Serializable id)

    OrgUser save(OrgUser orgUser)

}