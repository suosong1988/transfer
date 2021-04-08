package com.transfer.example.executor;

import com.transfer.converter.SqlExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * sql执行器
 */
@Component
public class DefaultSqlExecutor implements SqlExecutor {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object executeSelect(String sql) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void executeInsert(String sql) {
        jdbcTemplate.update(sql);
    }

    @Override
    public void executeUpdate(String sql) {
        jdbcTemplate.update(sql);
    }
}
