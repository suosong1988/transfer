package com.transfer;

import com.transfer.converter.FieldConverter;
import com.transfer.converter.InitDefaultValueIfNotExist;
import com.transfer.converter.KeyConverter;


import java.util.ArrayList;
import java.util.List;

/**
 * 这个是对xml配置数据的抽象
 * 生成sql语句的核心定义类
 */

public class SqlDefinition {
    //来源表名
    String fromTableName;
    //目的表名
    String toTableName;
    //两边数据源靠这个关联
    List<KeyMatchRole> keyMatchRoles = new ArrayList<>();
    //匹配的字段信息
    List<MatchFieldInfo> matchFields = new ArrayList<>();
    //有默认值的字段
    List<DefaultFieldInfo> defaultValueFields = new ArrayList<>();

    //在执行sql之前执行
    List<SqlBefore> sqlBefores = new ArrayList<>();
    //在执行sql之后执行
    List<SqlAfter> sqlAfters = new ArrayList<>();

    //禁止insert
    //private boolean forbidInsert;

   /* KeyMatchRole getKeyMatchRole(String fromKey, String toKey) {
        for (KeyMatchRole keyMatchRole : this.keyMatchRoles) {
            if (keyMatchRole.fromKey.equals(fromKey) && keyMatchRole.toKey.equals(toKey)) {
                return keyMatchRole;
            }
        }
        throw new RuntimeException("没找到KeyMatch,这是不允许的");
    }*/

    /**
     * 如果是insert操作，可以提供有些字段的默认值
     * 默认值字段信息
     */
    public static class DefaultFieldInfo {
        String fieldName;
        //默认值
        String defaultValue;
        boolean isNumber;
        //如果没有提供默认值，那么执行这个获取默认值
        InitDefaultValueIfNotExist initDefaultValueIfNotExist;
    }


    /**
     * 对应字段的转换信息
     */
    public static class MatchFieldInfo {
        String fromFieldName;
        String toFieldName;
        /**
         * 处理字段转化
         */
        FieldConverter fieldConverter;
        boolean isNumber;

        public MatchFieldInfo(String fromFieldName, String toFieldName, FieldConverter fieldConverter, boolean isNumber) {
            this.fromFieldName = fromFieldName;
            this.toFieldName = toFieldName;
            this.fieldConverter = fieldConverter;
            this.isNumber = isNumber;
        }
    }

    /**
     * 两边数据的匹配规则
     */
    public static class KeyMatchRole {
        // 来源表主键
        String fromKey;
        // 目的表主键
        String toKey;
        // key转化器
        KeyConverter keyConverter;
        // toKey是否为数字类型
        boolean isNumber;


        public KeyMatchRole(String fromKey, String toKey, KeyConverter keyConverter, boolean isNumber) {
            this.fromKey = fromKey;
            this.toKey = toKey;
            this.keyConverter = keyConverter;
            this.isNumber = isNumber;
        }
    }

}
