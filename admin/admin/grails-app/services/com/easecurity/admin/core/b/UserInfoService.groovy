package com.easecurity.admin.core.b

import grails.gorm.services.Service

@Service(UserInfo)
interface UserInfoService {

    UserInfo get(Serializable id)

    List<UserInfo> list(Map args)

    Long count()

    void delete(Serializable id)

    UserInfo save(UserInfo userInfo)

}