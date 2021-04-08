package com.transfer;

import com.transfer.enums.SqlType;

import java.util.Map;

/**
 * 执行完sql之后
 */
public interface SqlAfter {
    /**
     * @param fromTableMap
     * @throws
     */
    void sqlAfter(Map<String, Object> fromTableMap, String fromTable,
                  String toTable, SqlType sqlType);

}
