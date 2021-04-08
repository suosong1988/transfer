package com.transfer.example.sqlbefore;

import com.transfer.SqlBefore;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
public class AppleSqlBefore implements SqlBefore {
    @Override
    public boolean sqlBefore(Map<String, Object> fromFieldMap, String fromTableName, String toTableName) {

        if (fromFieldMap.get("name").equals("苹果")) {
            return false;
        }
        return true;
    }
}
