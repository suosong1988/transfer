package com.transfer.example.keyconverter;

import com.transfer.converter.KeyConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 产品表id匹配转化器
 */
@Component
public class ProductIdConverter implements KeyConverter {
    @Override
    public Object convert(String fromKey, Map<String, Object> fromFieldMap) {

        return "product_" + fromFieldMap.get(fromKey);
    }
}
