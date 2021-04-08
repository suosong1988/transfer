package com.transfer;

/**
 * suosong
 * 2021/4/3
 */
public class SelectSqlObj {
    private String sql;
    //where片段
    private String whereSql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }
}
