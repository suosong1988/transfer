package com.transfer;

import com.transfer.converter.SqlExecutor;

/**
 * suosong
 * 2021/4/3
 */
public class SqlExecutorTest implements SqlExecutor {
    @Override
    public Object executeSelect(String sql) {
        long time = System.currentTimeMillis();
        if (time % 2 == 0) {
            return new Object();
        }
        return null;
    }

    @Override
    public void executeInsert(String sql) {

    }

    @Override
    public void executeUpdate(String sql) {

    }
}
