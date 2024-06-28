/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.lang.Nullable;

import com.easecurity.db.BaseEnum;

/**
 * 实现BaseEnum接口的通用枚举类型数据转换器（JdbcTemplate模式下）
 * @param <T>
 *
 */
public class BeanPropertyRowMapper<T> extends org.springframework.jdbc.core.BeanPropertyRowMapper<T> {

    public BeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        super(mappedClass, checkFullyPopulated);
    }

    @Override
    protected void initialize(Class<T> mappedClass) {
        super.initialize(mappedClass);
        ((DefaultConversionService) super.getConversionService()).addConverterFactory(new StringToBaseEnumConverterFactory());
        ((DefaultConversionService) super.getConversionService()).addConverter(new BaseEnumToStringConverter());
    }
    
    public static class BaseEnumToStringConverter implements Converter<BaseEnum, String> {

        @Override
        public String convert(BaseEnum source) {
            return source.getId();
        }

    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

        @Override
        public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
            return new StringToEnum(ConversionUtils.getEnumType(targetType));
        }

        private static class StringToEnum<T extends Enum> implements Converter<String, T> {

            private final Class<T> enumType;
            private BaseEnum[] list;

            StringToEnum(Class<T> enumType) {
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
            @Nullable
            public T convert(String source) {
                if (source.isEmpty()) {
                    // It's an empty enum identifier: reset the enum value to null.
                    return null;
                }
                try {
                    if (list != null && list.length > 0)
                        return (T) Stream.of(list).filter(p -> p.getId().equals(source)).findFirst().orElseThrow(IllegalArgumentException::new);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return (T) Enum.valueOf(this.enumType, source.trim());
            }
        }
    }
}
