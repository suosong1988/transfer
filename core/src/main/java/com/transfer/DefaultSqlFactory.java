package com.transfer;

import com.google.common.collect.Sets;
import com.transfer.converter.KeyConverter;
import com.transfer.converter.SqlExecutor;
import com.transfer.enums.SqlType;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 生成sql语句
 */
public class DefaultSqlFactory implements SqlFactory {

    private SqlExecutor sqlExecutor;

    public DefaultSqlFactory(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    private Map<String, SqlDefinition> definitionMap = new HashMap<>();
    //表名映射,要求按照顺序来
    private Map<String, List<String>> fromToTableNames = new LinkedHashMap<>();


    @Deprecated
    private String checkNumber(boolean isNumber, String key, Object value) {
        //将value中的单' 换成''
        String filterValue = "";
        if (value != null) {
            filterValue = value.toString().replace("'", "''");
        } else {
            return " ";
        }
        if (isNumber) {
            return key + " = " + filterValue;
        } else {
            return key + " = '" + filterValue + "'";
        }
    }


    @Override
    public List<SqlObj> getExecuteSql(Map<String, Object> fromTableMap, String fromTableName, String toTableName) {
        List<SqlObj> sqlObjs = new ArrayList<>();
        SqlDefinition definition = this.getSqlDefinition(fromTableName, toTableName);
        List<SelectSqlObj> selectSqls = this.getToTableSelectSql(fromTableMap, fromTableName, toTableName);
        for (SelectSqlObj selectSql : selectSqls) {
            SqlObj sqlObj = new SqlObj();
            sqlObjs.add(sqlObj);
            Object o = sqlExecutor.executeSelect(selectSql.getSql());
            String sql = null;
            if (o == null) {
                sqlObj.setSqlType(SqlType.INSERT);
                sql = "insert into " + toTableName + getMatchFieldSql(fromTableMap, definition);
                String defaultValueSql = this.getDefaultValueSql(fromTableMap, definition);
                if (defaultValueSql != null) {
                    sql = sql + "," + defaultValueSql;
                }
                sql = sql + "," + selectSql.getWhereSql().replaceFirst("where", " ").replace("and", ",");
            } else {
                sqlObj.setSqlType(SqlType.UPDATE);
                sql = "update " + toTableName + getMatchFieldSql(fromTableMap, definition);
                sql = sql + selectSql.getWhereSql();
            }

            sqlObj.setSql(sql);
        }
        return sqlObjs;
    }

    /**
     * 获取sql数据中段
     *
     * @param fromTableMap
     * @param sqlDefinition
     * @return
     */
    private String getMatchFieldSql(Map<String, Object> fromTableMap, SqlDefinition sqlDefinition) {

        String sql = " set ";
        for (SqlDefinition.MatchFieldInfo matchField : sqlDefinition.matchFields) {
            Object toValue = matchField.fieldConverter.convert(matchField.fromFieldName, matchField.toFieldName, fromTableMap.get(matchField.fromFieldName), fromTableMap);
            if (matchField.isNumber) {
                sql = sql + matchField.toFieldName + "=" + toValue + ",";
            } else {
                sql = sql + matchField.toFieldName + "='" + toValue + "',";
            }
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        return sql;
    }

    @Override
    public List<SelectSqlObj> getToTableSelectSql(Map<String, Object> fromTableMap, String fromTableName, String toTableName) {
        SqlDefinition definition = this.getSqlDefinition(fromTableName, toTableName);
        Set<List<SqlDefinition.KeyMatchRole>> whereConditions = this.getWhereConditions(fromTableMap, definition);
        List<String> whereSqlStrs = new ArrayList<>();
        for (List<SqlDefinition.KeyMatchRole> whereCondition : whereConditions) {
            whereSqlStrs.add(this.getWhereSql(whereCondition, fromTableMap));
        }
        List<SelectSqlObj> selectSqlObjs = new ArrayList<>();
        for (String whereSqlStr : whereSqlStrs) {
            String selectSql = "select * from " + toTableName + whereSqlStr;
            SelectSqlObj obj = new SelectSqlObj();
            selectSqlObjs.add(obj);
            obj.setSql(selectSql);
            obj.setWhereSql(whereSqlStr);
        }
        return selectSqlObjs;
    }

    /**
     * 获取where条件
     *
     * @param fromTableMap
     * @param definition
     * @return
     */
    private Set<List<SqlDefinition.KeyMatchRole>> getWhereConditions(Map<String, Object> fromTableMap, SqlDefinition definition) {
        Map<String/*toKey*/, Set<SqlDefinition.KeyMatchRole>> entryMap = new HashMap<>();
        //两表关联字段
        for (SqlDefinition.KeyMatchRole keyConverterRole : definition.keyMatchRoles) {
            String toKey = keyConverterRole.toKey;
            KeyConverter keyConverter = keyConverterRole.keyConverter;
            if (!entryMap.containsKey(toKey)) {
                entryMap.put(toKey, new HashSet<>());
            }
            Set<SqlDefinition.KeyMatchRole> toKeyRoleSet = entryMap.get(toKey);

            toKeyRoleSet.add(keyConverterRole);
        }
        //求笛卡尔积
        List<Set<SqlDefinition.KeyMatchRole>> setList = new ArrayList<>();
        for (String toKey : entryMap.keySet()) {
            setList.add(entryMap.get(toKey));
        }
        Set<List<SqlDefinition.KeyMatchRole>> cartesianProduct = Sets.cartesianProduct(setList);
        return cartesianProduct;

    }

    private String getWhereSql(List<SqlDefinition.KeyMatchRole> list, Map<String, Object> fromTableMap) {
        //求where语句
        String sql = " where ";
        for (SqlDefinition.KeyMatchRole keyMatchRole : list) {
            Object toValue = keyMatchRole.keyConverter.convert(keyMatchRole.fromKey, fromTableMap);
            if (keyMatchRole.isNumber) {
                sql = sql + " " + keyMatchRole.toKey + "=" + toValue + " and ";
            } else {
                sql = sql + " " + keyMatchRole.toKey + "='" + toValue + "' and ";
            }
        }
        sql = sql.substring(0, sql.lastIndexOf("and"));
        return sql;
    }


    @Override
    public void registerSqlDefinition(SqlDefinition sqlDefinition) {

        this.definitionMap.put(getSqlKey(sqlDefinition.fromTableName, sqlDefinition.toTableName), sqlDefinition);

        String fromTableName = sqlDefinition.fromTableName;
        String toTableName = sqlDefinition.toTableName;
        List<String> toTableNames = fromToTableNames.get(fromTableName);
        if (toTableNames == null) {
            toTableNames = new ArrayList<>();
            fromToTableNames.put(fromTableName, toTableNames);
        }
        toTableNames.add(toTableName);
    }

    @Override
    public Set<String> getAllFromTableNames() {
        return this.fromToTableNames.keySet();
    }


    @Override
    public List<String> getToTableNames(String fromTableName) {
        return this.fromToTableNames.get(fromTableName);
    }

    private String getSqlKey(String fromTableName, String toTableName) {
        return fromTableName + "#" + toTableName;
    }

    private SqlDefinition getSqlDefinition(String fromTableName, String toTableName) {
        return this.definitionMap.get(getSqlKey(fromTableName, toTableName));
    }

    @Override
    public List<SqlBefore> getAllSqlBefores(String fromTableName, String toTableName) {
        SqlDefinition sqlDefinition = this.getSqlDefinition(fromTableName, toTableName);
        return sqlDefinition.sqlBefores;
    }

    @Override
    public List<SqlAfter> getAllSqlAfters(String fromTableName, String toTableName) {
        SqlDefinition sqlDefinition = this.getSqlDefinition(fromTableName, toTableName);
        return sqlDefinition.sqlAfters;
    }

    /**
     * 获取defaultValue的sql片段
     *
     * @param fromFieldMap
     * @param
     * @return
     */
    private String getDefaultValueSql(Map<String, Object> fromFieldMap, SqlDefinition definition) {
        String sql = "";
        if (CollectionUtils.isEmpty(definition.defaultValueFields)) {
            return null;
        }
        //替换默认字段
        for (SqlDefinition.DefaultFieldInfo defaultFieldInfo : definition.defaultValueFields) {
            Object defaultValue = defaultFieldInfo.defaultValue;
            if (defaultValue == null) {
                defaultValue = defaultFieldInfo.initDefaultValueIfNotExist.init(fromFieldMap);
            }
            if (defaultFieldInfo.isNumber) {
                sql = sql + defaultFieldInfo.fieldName + "=" + defaultValue + ",";
            } else {
                sql = sql + defaultFieldInfo.fieldName + "='" + defaultValue + "',";
            }

        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        return sql;
    }
}
