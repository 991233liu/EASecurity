/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.model.convert.internal.NamedEnumValueConverter;
import org.hibernate.metamodel.model.convert.spi.EnumValueConverter;
import org.hibernate.type.EnumType;
import org.hibernate.type.descriptor.java.EnumJavaTypeDescriptor;

import com.easecurity.db.BaseEnum;

/**
 * 实现BaseEnum接口的枚举类型数据处理类（hibernate的xml模式下）
 *
 */
@SuppressWarnings("rawtypes")
public class BaseEnumType extends EnumType<Enum> {

    private static final long serialVersionUID = -728251571404006802L;

    private EnumValueConverter enumValueConverter;

    @Override
    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties parameters) {
        super.setParameterValues(parameters);
        if (isImplementingInterface(super.returnedClass(), BaseEnum.class)) {
            this.enumValueConverter = new NamedEnumValueConverter(new BaseEnumTypeDescriptor(super.returnedClass()));
        }
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        if (this.enumValueConverter != null) {
            return enumValueConverter.readValue(rs, names[0], session);
        }
        return super.nullSafeGet(rs, names, session, owner);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (this.enumValueConverter != null) {
            enumValueConverter.writeValue(st, (Enum) value, index, session);
        }
        super.nullSafeSet(st, value, index, session);
    }

    /**
     * 判断给定类是否实现了指定的接口。
     *
     * @param clazz          需要检查的类的Class对象。
     * @param interfaceClass 指定的接口的Class对象。
     * @return 如果clazz实现了interfaceClass，则返回true，否则返回false。
     */
    private boolean isImplementingInterface(Class<?> clazz, Class<?> interfaceClass) {
        // 获取类实现的所有接口
        Class<?>[] interfaces = clazz.getInterfaces();

        // 遍历接口数组，检查是否包含指定的接口
        for (Class<?> implementedInterface : interfaces) {
            if (implementedInterface.equals(interfaceClass)) {
                return true;
            }
        }

        // 如果当前类没有直接实现指定接口，检查其超类是否实现了该接口
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            return isImplementingInterface(superclass, interfaceClass);
        }

        return false;
    }

    public static class BaseEnumTypeDescriptor<T extends Enum> extends EnumJavaTypeDescriptor<T> {

        private static final long serialVersionUID = 1278633260471472545L;

        private BaseEnum[] list;

        @SuppressWarnings("unchecked")
        public BaseEnumTypeDescriptor(Class type) {
            super(type);
            try {
                Method staticMethod = type.getMethod("values");
                list = (BaseEnum[]) staticMethod.invoke(null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString(T value) {
            return toName(value);
        }

        @Override
        public T fromString(String string) {
            return fromName(string);
        }

        @Override
        @SuppressWarnings("unchecked")
        public T fromName(String relationalForm) {
            if (relationalForm == null) {
                return null;
            }
            return (T) Stream.of(list).filter(p -> p.getId().equals(relationalForm)).findFirst().orElseThrow(IllegalArgumentException::new);
        }

        @Override
        public String toName(T domainForm) {
            if (domainForm == null) {
                return null;
            }
            return ((BaseEnum) domainForm).getId();
        }
    }
}
