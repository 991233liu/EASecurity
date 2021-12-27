package com.easecurity.admin.core.b

import grails.gorm.services.Service

@Service(Posts)
interface PostsService {

    Posts get(Serializable id)

    List<Posts> list(Map args)

    Long count()

    void delete(Serializable id)

    Posts save(Posts posts)

}