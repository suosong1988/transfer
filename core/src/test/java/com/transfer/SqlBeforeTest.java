package com.transfer;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/3
 */
@Component
public class SqlBeforeTest implements SqlBefore{
    @Override
    public boolean sqlBefore(Map<String, Object> fromFieldMap, String fromTableName, String toTableName) {
        return true;
    }
}
