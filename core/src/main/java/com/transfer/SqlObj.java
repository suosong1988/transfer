package com.transfer;

import com.transfer.enums.SqlType;


/**
 * suosong
 * 2021/4/3
 */
public class SqlObj {
    private String sql;
    private SqlType sqlType;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }
}
