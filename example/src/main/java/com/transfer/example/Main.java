package com.transfer.example;

import com.transfer.DefaultSqlFactory;
import com.transfer.SqlFactory;
import com.transfer.XmlSqlReader;
import com.transfer.converter.SqlExecutor;
import com.transfer.example.config.Config;
import com.transfer.example.executor.DefaultSqlExecutor;
import com.transfer.transfer.Transfer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * suosong
 * 2021/4/7
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        SqlExecutor sqlExecutor = applicationContext.getBean(DefaultSqlExecutor.class);
        SqlFactory factory = new DefaultSqlFactory(sqlExecutor);
        XmlSqlReader sqlReader = new XmlSqlReader(factory);
        //解析xml
        sqlReader.loadResource(new ClassPathResource("example.xml"), applicationContext);
        //搬运工
        Transfer transfer = new Transfer(factory, sqlExecutor);
        /**
         * 搬运数据
         */
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from old_product");
        for (Map<String, Object> map : list) {
            for (String toTableName : transfer.getToTableNames("old_product")) {
                transfer.transfer(map, "old_product", toTableName);
            }

        }


    }
}
