package com.easecurity.core.db;

import org.springframework.core.convert.converter.Converter;

import com.easecurity.db.BaseEnum;

public class BaseEnumToStringConverter implements Converter<BaseEnum, String> {

    @Override
    public String convert(BaseEnum source) {
        return source.getId();
    }

}