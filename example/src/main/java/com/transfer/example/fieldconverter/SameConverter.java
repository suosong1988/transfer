package com.transfer.example.fieldconverter;

import com.transfer.converter.FieldConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 返回原值
 */
@Component
public class SameConverter implements FieldConverter {
    @Override
    public String convert(String fromFieldName, String toFieldName, Object sourceFieldValue, Map<String, Object> fromFieldMap) {
        return sourceFieldValue.toString();
    }
}
