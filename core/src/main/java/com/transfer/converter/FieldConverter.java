package com.transfer.converter;

import java.util.Map;

/**
 * 字段之间转化
 */
public interface FieldConverter {
    /**
     * 字段之间转化
     */
    String convert(String fromFieldName, String toFieldName, Object sourceFieldValue, Map<String, Object> fromFieldMap);

}
