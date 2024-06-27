/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.db;

import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author liulf
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
}
