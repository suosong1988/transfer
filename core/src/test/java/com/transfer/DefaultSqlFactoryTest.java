package com.transfer;

import com.transfer.converter.SqlExecutor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultSqlFactoryTest {

    private SqlFactory sqlFactory;

    private Map<String, Object> fromMap = new HashMap<>();


    @Before
    public void setup() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigTest.class);
        SqlExecutor sqlExecutor = new SqlExecutorTest();
        this.sqlFactory = new DefaultSqlFactory(sqlExecutor);
        XmlSqlReader xmlReader = new XmlSqlReader(this.sqlFactory);
        xmlReader.loadResource(new ClassPathResource("xml/developer.xml"), applicationContext);

        //初始化来源数据
        fromMap.put("fromKey", "fromKey");
        fromMap.put("from", "fromFieldName");

    }


    @Test
    public void test_select() {
        System.out.println(sqlFactory.getToTableSelectSql(fromMap, "fromTable", "toTable"));
    }


    @Test
    public void test_executeSql() {
        System.out.println("=============================");
        List<SqlObj> sqlObjs = sqlFactory.getExecuteSql(fromMap, "fromTable", "toTable");
        for (SqlObj sqlObj : sqlObjs) {
            System.out.println(sqlObj.getSql());
        }

    }

}