package com.easecurity.core.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;

import com.easecurity.db.BaseEnum;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

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
