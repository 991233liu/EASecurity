package com.easecurity.admin.core

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

/**
 * 通用主键生成器，String类型，16位UUID
 */
class UUID8IdentifierGenerator implements IdentifierGenerator {

    @Override
    Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, 8)
    }
}