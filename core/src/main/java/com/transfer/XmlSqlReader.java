package com.transfer;


import com.transfer.converter.FieldConverter;
import com.transfer.converter.InitDefaultValueIfNotExist;
import com.transfer.converter.KeyConverter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;


import java.io.InputStream;
import java.util.Iterator;

/**
 * suosong
 * 2019/3/24
 */
public class XmlSqlReader implements SqlReader {

    private final String FROM_TABLE = "fromTable";
    private final String TO_TABLE = "toTable";
    private final String TABLE_NAME = "tableName";


    private final String MATCH_FIELDS = "matchFields";

    private final String FROM = "from";
    private final String TO = "to";
    private final String FIELD_CONVERTER = "fieldConverter";
    private final String DEFAULT_FIELDS = "defaultFields";

    private final String FROM_KEY = "fromKey";
    private final String TO_KEY = "toKey";

    private final String KEY_CONVERTER = "keyConverter";


    private final String Key_MatchRoles = "keyMatchRoles";
    private final String SQL_BEFORE = "sqlBefore";
    private final String SQL_AFTER = "sqlAfter";
    private final String IS_NUMBER = "isNumber";


    private final String DEFAULT_VALUE = "defaultValue";
    private final String FIELD_NAME = "fieldName";


    private SqlFactory sqlFactory;

    public XmlSqlReader(SqlFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }

    @Override
    public void loadResource(Resource resource, ApplicationContext applicationContext) {
        try {
            InputStream inputStream = resource.getInputStream();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();
            Iterator<Element> it = root.elementIterator();
            while (it.hasNext()) {
                Element ele = it.next();
                SqlDefinition sqlDefinition = new SqlDefinition();
                parseTransfer(ele, sqlDefinition, applicationContext);
                this.sqlFactory.registerSqlDefinition(sqlDefinition);
            }
        } catch (Exception e) {
            throw new RuntimeException("xml解析失败", e);
        }
    }

    /**
     * 解析fromTable标签
     *
     * @param ele
     */
    private void parseTransfer(Element ele, SqlDefinition sqlDefinition, ApplicationContext context) {
        //解析sqlBefore 与  sqlAfter
        String sqlBeforeStr = ele.attributeValue(SQL_BEFORE);
        String sqlAfterStr = ele.attributeValue(SQL_AFTER);


        if (!ObjectUtils.isEmpty(sqlBeforeStr)) {
            for (String before : sqlBeforeStr.split(",")) {
                sqlDefinition.sqlBefores.add((SqlBefore) context.getBean(before));
            }
        }
        if (!ObjectUtils.isEmpty(sqlAfterStr)) {
            for (String after : sqlAfterStr.split(",")) {
                sqlDefinition.sqlAfters.add((SqlAfter) context.getBean(after));
            }
        }
        Element fromTableEle = ele.element(FROM_TABLE);
        Element toTableEle = ele.element(TO_TABLE);
        parseFromTable(fromTableEle, sqlDefinition);
        parseToTable(toTableEle, sqlDefinition, context);
    }

    private void parseFromTable(Element fromTableEle, SqlDefinition definition) {
        String fromTableName = fromTableEle.attributeValue(TABLE_NAME);
        definition.fromTableName = fromTableName;
    }

    private void parseToTable(Element toTableEle, SqlDefinition definition, ApplicationContext context) {
        definition.toTableName = toTableEle.attributeValue(TABLE_NAME);
        //解析两边数据源对应的key
        Element toJoinKeysEle = toTableEle.element(Key_MatchRoles);
        Iterator<Element> toJoinKeysIter = toJoinKeysEle.elementIterator();
        while (toJoinKeysIter.hasNext()) {
            Element ToJoinKeyEle = toJoinKeysIter.next();
            String fromKey = ToJoinKeyEle.attributeValue(FROM_KEY);
            String toKey = ToJoinKeyEle.attributeValue(TO_KEY);
            boolean isNumber = Boolean.parseBoolean(ToJoinKeyEle.attributeValue(IS_NUMBER));

            KeyConverter keyConverter = (KeyConverter) context.getBean(ToJoinKeyEle.attributeValue(KEY_CONVERTER));
            definition.keyMatchRoles.add(new SqlDefinition.KeyMatchRole(fromKey, toKey, keyConverter, isNumber));
        }
        //matchFields
        Element matchFieldsEle = toTableEle.element(MATCH_FIELDS);
        Iterator<Element> matchFieldEles = matchFieldsEle.elementIterator();
        while (matchFieldEles.hasNext()) {
            Element matchFieldEle = matchFieldEles.next();
            String from = matchFieldEle.attributeValue(FROM);
            String to = matchFieldEle.attributeValue(TO);
            String fieldConverterName = matchFieldEle.attributeValue(FIELD_CONVERTER);
            FieldConverter fieldConverter = context.getBean(fieldConverterName, FieldConverter.class);
            Boolean isNumber = Boolean.parseBoolean(matchFieldEle.attributeValue(IS_NUMBER));
            SqlDefinition.MatchFieldInfo convertFieldInfo = new SqlDefinition.MatchFieldInfo(from, to, fieldConverter, isNumber);
            definition.matchFields.add(convertFieldInfo);
        }

        //defaultFields
        Element defaultFieldsEle = toTableEle.element(DEFAULT_FIELDS);
        Iterator<Element> defaultFieldEles = defaultFieldsEle.elementIterator();
        while (defaultFieldEles.hasNext()) {
            Element defaultFieldEle = defaultFieldEles.next();
            String fieldName = defaultFieldEle.attributeValue(FIELD_NAME);
            String defaultValue = defaultFieldEle.attributeValue(DEFAULT_VALUE);
            String initDefaultValueIfNotExistBeanName = defaultFieldEle.attributeValue("initDefaultValueIfNotExist");
            boolean isNumber = Boolean.parseBoolean(defaultFieldEle.attributeValue(IS_NUMBER));
            SqlDefinition.DefaultFieldInfo defaultFieldInfo = new SqlDefinition.DefaultFieldInfo();
            defaultFieldInfo.fieldName = fieldName;
            defaultFieldInfo.defaultValue = defaultValue;
            defaultFieldInfo.isNumber = isNumber;
            if (initDefaultValueIfNotExistBeanName != null) {
                defaultFieldInfo.initDefaultValueIfNotExist = context.getBean(initDefaultValueIfNotExistBeanName, InitDefaultValueIfNotExist.class);
            }
            definition.defaultValueFields.add(defaultFieldInfo);
        }
    }
}
