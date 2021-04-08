package com.transfer;

import com.transfer.converter.KeyConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/3
 */
@Component
public class KeyConverterTest3 implements KeyConverter {
    @Override
    public Object convert(String value, Map<String, Object> fromFieldMap) {
        return "王五 keyConverter";
    }
}
