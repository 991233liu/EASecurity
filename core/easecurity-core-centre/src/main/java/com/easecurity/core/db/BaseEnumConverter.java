/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.easecurity.db.BaseEnum;

/**
 * 实现BaseEnum接口的通用枚举类型数据转换器（jpa模式下）
 *
 * @param <T>
 */
@Converter
public class BaseEnumConverter<T extends Enum<T>> implements AttributeConverter<BaseEnum, String> {

    private final Class<T> enumType;
    private BaseEnum[] list;

    public BaseEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
        try {
            Method staticMethod = enumType.getMethod("values");
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
    public String convertToDatabaseColumn(BaseEnum category) {
        if (category == null) {
            return null;
        }
        return category.getId();
    }

    @Override
    public BaseEnum convertToEntityAttribute(String code) {
        if (code.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        try {
            if (list != null && list.length > 0)
                return (BaseEnum) Stream.of(list).filter(p -> p.getId().equals(code)).findFirst().orElseThrow(IllegalArgumentException::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (BaseEnum) Enum.valueOf(this.enumType, code.trim());
    }
}
