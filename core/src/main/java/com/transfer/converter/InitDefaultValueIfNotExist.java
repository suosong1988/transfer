package com.transfer.converter;

import java.util.Map;

public interface InitDefaultValueIfNotExist {
    /**
     * 初始化字段
     *
     * @param sourceFields
     * @return
     */
    String init(Map<String, Object> sourceFields);

}
