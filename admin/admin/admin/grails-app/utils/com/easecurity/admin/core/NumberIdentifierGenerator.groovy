package com.easecurity.admin.core

import com.easecurity.distributedid.DistributeIdFactory
import com.easecurity.util.MBeanUtils
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

/**
 * 通用主键生成器，number类型，雪花算法
 */
class NumberIdentifierGenerator implements IdentifierGenerator {

    private static Integer mId = MBeanUtils.showJvmInfo(null).get("jvm_name").hashCode().abs() % 511

    @Override
    Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return DistributeIdFactory.getSnowFlake(0, 2, mId).nextId()
    }
}