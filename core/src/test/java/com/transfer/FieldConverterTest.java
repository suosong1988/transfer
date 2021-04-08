package com.transfer;

import com.transfer.converter.FieldConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/3
 */
@Component
public class FieldConverterTest implements FieldConverter {
    @Override
    public String convert(String fromFieldName, String toFieldName, Object sourceFieldValue, Map<String, Object> fromFieldMap) {
        return "李四 fieldConverter";
    }
}
