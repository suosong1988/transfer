package com.transfer.example.fieldconverter;

import com.transfer.converter.FieldConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 商品名称转化器
 */
@Component
public class ProductNameConverter implements FieldConverter {
    @Override
    public String convert(String fromFieldName, String toFieldName, Object sourceFieldValue, Map<String, Object> fromFieldMap) {
        if (sourceFieldValue.toString().equals("菠菜")) {
            return "库尔勒菠菜";
        }
        return sourceFieldValue.toString();
    }
}
