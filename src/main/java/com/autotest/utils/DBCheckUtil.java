package com.autotest.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBCheckUtil {

    /**
     * 根据脚本执行并返回查询结果
     * @param dataSql 需要执行的查询语句
     * @return 返回查询结果
     */
    public  static  String doQuery(String dataSql){
        // 脚本字符串封装成对象
        List<DBCheck> dbChecks = JSONObject.parseArray(dataSql, DBCheck.class);
        List<DBQueryResult> dbQueryResults = new ArrayList<>();
        for (DBCheck dbCheck : dbChecks) {
            String no = dbCheck.getNo() ;
            String sql = dbCheck.getSql();
            // 执行查询，获取结果
            Map<String,Object> columnLabelAndValues = JDBCUtil.query(sql);
            // 封装成查询结果对象
            DBQueryResult dbQueryResult = new DBQueryResult();
            dbQueryResult.setNo(no);
            dbQueryResult.setColumnLabelAndValues(columnLabelAndValues);
            dbQueryResults.add(dbQueryResult);
        }
        return JSONObject.toJSONString(dbQueryResults);
    }
}

class DBCheck{
    private String no;
    private String sql;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

/**
 * 数据库查询结果实体类
 */
class DBQueryResult{
    private String no;
    private Map<String, Object> columnLabelAndValues;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Map<String, Object> getColumnLabelAndValues() {
        return columnLabelAndValues;
    }

    public void setColumnLabelAndValues(Map<String, Object> columnLabelAndValues) {
        this.columnLabelAndValues = columnLabelAndValues;
    }
}
