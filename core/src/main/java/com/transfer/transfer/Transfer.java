package com.transfer.transfer;

import com.transfer.SqlAfter;
import com.transfer.SqlBefore;
import com.transfer.SqlFactory;
import com.transfer.SqlObj;
import com.transfer.converter.SqlExecutor;
import com.transfer.enums.SqlType;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 数据搬运的主体类
 */
public class Transfer {


    private SqlFactory factory;

    private SqlExecutor sqlExecutor;

    public Transfer(SqlFactory factory, SqlExecutor sqlExecutor) {
        this.factory = factory;
        this.sqlExecutor = sqlExecutor;
    }

    public List<String> getToTableNames(String fromTableName){
        return factory.getToTableNames(fromTableName);
    }

    public void transfer(Map<String, Object> fromTableMap, String fromTableName, String toTableName) {
        /**
         * sql执行之前
         */
        List<SqlBefore> sqlBefores = factory.getAllSqlBefores(fromTableName, toTableName);
        if (!CollectionUtils.isEmpty(sqlBefores)) {
            for (SqlBefore sqlBefore : sqlBefores) {
                if (!sqlBefore.sqlBefore(fromTableMap, fromTableName, toTableName)) {
                    return;
                }
            }
        }
        /**
         * 执行sql
         */
        List<SqlObj> executeSql = factory.getExecuteSql(fromTableMap, fromTableName, toTableName);
        for (SqlObj sqlObj : executeSql) {
            if (sqlObj.getSqlType() == SqlType.INSERT) {
                sqlExecutor.executeInsert(sqlObj.getSql());
            } else if (sqlObj.getSqlType() == SqlType.UPDATE) {
                sqlExecutor.executeUpdate(sqlObj.getSql());
            }
            /**
             * sql执行之后
             */
            List<SqlAfter> sqlAfters = factory.getAllSqlAfters(fromTableName, toTableName);
            if (!CollectionUtils.isEmpty(sqlAfters)) {
                for (SqlAfter sqlAfter : sqlAfters) {
                    sqlAfter.sqlAfter(fromTableMap, fromTableName, toTableName, sqlObj.getSqlType());
                }
            }
        }
    }

}
