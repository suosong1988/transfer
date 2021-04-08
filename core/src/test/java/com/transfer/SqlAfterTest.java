package com.transfer;

import com.transfer.enums.SqlType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/3
 */
@Component
public class SqlAfterTest implements SqlAfter{
    @Override
    public void sqlAfter(Map<String, Object> fromFieldMap,  String fromTable, String toTable, SqlType sqlType) {

    }
}
