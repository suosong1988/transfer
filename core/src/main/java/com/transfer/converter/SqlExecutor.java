package com.transfer.converter;

/**
 * suosong
 * 2021/4/3
 */
public interface SqlExecutor {

    Object executeSelect(String sql);

    void executeInsert(String sql);

    void executeUpdate(String sql);
}
