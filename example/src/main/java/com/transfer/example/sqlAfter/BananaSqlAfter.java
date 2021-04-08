package com.transfer.example.sqlAfter;

import com.transfer.SqlAfter;
import com.transfer.enums.SqlType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * suosong
 * 2021/4/8
 */
@Component
public class BananaSqlAfter implements SqlAfter {
    @Override
    public void sqlAfter(Map<String, Object> fromTableMap, String fromTable, String toTable, SqlType sqlType) {

        if (fromTableMap.get("name").equals("香蕉")) {
            if (sqlType == SqlType.UPDATE) {
                System.out.println("=============香蕉被修改了");
            } else {
                System.out.println("=============香蕉被插入了");
            }
        }

    }
}
