package com.autotest.variable;

import com.autotest.utils.JDBCUtil;

import java.util.Map;

public class VariableGenerator {

    /**
     * 生成用于注册的手机号
     * @return 返回未注册手机号
     */
    public static String generateRegisterMobile(){
        String sql = "SELECT concat(max(mobile_phone)+1,'') registerMobile FROM member";
        Map<String, Object> columnLabelAndValue = JDBCUtil.query(sql);
        return columnLabelAndValue.get("registerMobile").toString();
    }

    /**
     * 生成注册成功后的id
     * @return 返回注册成功后的id
     */
    public static String generateMemberId(){
        String sql = "SELECT max(id)+1 id FROM member";
        Map<String, Object> columnLabelAndValue = JDBCUtil.query(sql);
        return columnLabelAndValue.get("id").toString();
    }

}
