package com.transfer.converter;

import java.util.Map;

/**
 * key 转化器
 */
public interface KeyConverter {

    /**
     *
     *
     * @param fromKey
     * @param fromFieldMap
     * @return
     */
    Object convert(String fromKey, Map<String, Object> fromFieldMap);

}
