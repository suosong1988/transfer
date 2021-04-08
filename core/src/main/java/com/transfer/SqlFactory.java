package com.transfer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SqlFactory {


    /**
     * 获取可以执行的sql列表
     *
     * @param fromTableMap
     * @param fromTableName
     * @param toTableName
     * @return
     */
    List<SqlObj> getExecuteSql(Map<String, Object> fromTableMap, String fromTableName, String toTableName);


    /**
     * 获得目标表select语句
     *
     * @param fromTableMap
     * @param
     * @param
     * @return
     */
    List<SelectSqlObj> getToTableSelectSql(Map<String, Object> fromTableMap, String fromTableName, String toTableName);


    /**
     * 注册SqlDefiniton
     *
     * @param definition
     */
    void registerSqlDefinition(SqlDefinition definition);


    Set<String> getAllFromTableNames();

    /**
     * 一个源表可能对应多个目标表
     *
     * @param fromTableName
     * @return
     */
    List<String> getToTableNames(String fromTableName);

    List<SqlBefore> getAllSqlBefores(String fromTableName, String toTableName);

    List<SqlAfter> getAllSqlAfters(String fromTableName, String toTableName);

}
