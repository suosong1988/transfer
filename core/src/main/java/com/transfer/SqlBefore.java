package com.transfer;

import java.util.Map;

public interface SqlBefore {
    /**
     * 在执行sql之前执行
     *
     * @param fromFieldMap  原始数据
     * @param fromTableName 数据来源
     * @param toTableName   数据去向
     * @return
     * @throws
     */
    boolean sqlBefore(Map<String, Object> fromFieldMap, String fromTableName, String toTableName);

}
