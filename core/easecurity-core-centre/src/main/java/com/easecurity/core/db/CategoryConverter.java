package com.easecurity.core.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

import com.easecurity.core.basis.b.UserEnum.AcStatus;
import com.easecurity.db.BaseEnum;

@Component
@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<BaseEnum, String> {
 
    @Override
    public String convertToDatabaseColumn(BaseEnum category) {
        if (category == null) {
            return null;
        }
//        return null;
        return category.getId();
    }

    @Override
    public BaseEnum convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
System.out.println(code);
System.out.println(code);
System.out.println(code);
System.out.println(code);
        return AcStatus.values()[0];
//        return Stream.of(AcStatus.values())
//          .filter(c -> c.getId().equals(code))
//          .findFirst()
//          .orElseThrow(IllegalArgumentException::new);
    }
}
