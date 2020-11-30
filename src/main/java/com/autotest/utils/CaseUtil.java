package com.autotest.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CaseUtil {

    // 保存所有的用例对象（共享数据）
    public static List<Case> cases = new ArrayList<>();

    // 将所有数据解析封装到cases
    static {
        cases = ExcelUtil.load(PropertiesUtil.getExcelPath(), "case", Case.class);
    }

    /**
     * 根据接口编号获取对应接口的用例参数数据
     * @param apiId 指定接口编号
     * @param cellNames 要获取的测试数据列名
     * @return 返回用例参数数据
     */
    public static Object[][] getCaseDatasByApiId(String apiId,String[] cellNames){
        // 保存指定接口编号的case对象
        ArrayList<Case> csList = new ArrayList<>();
        // 通过循环找出指定接口编号对应的数据
        for (Case cs : cases) {
            if (cs.getApiId().equals(apiId)){
                csList.add(cs);
            }
        }
        Object[][] datas = new Object[csList.size()][cellNames.length];
        Class<Case> clazz = Case.class;
        for (int i = 0; i < csList.size(); i++) {
            Case cs = csList.get(i);
            for (int j = 0; j < cellNames.length; j++) {
               // 要反射的方法名
                String methodName = "get" + cellNames[j];
                Method method;
                String value = "";
                try {
                    method = clazz.getMethod(methodName);
                    value = (String) method.invoke(cs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                datas[i][j] = value;
            }
        }
        return datas;
    }
}

/**
 * 保存case信息
 */
class Case{
    private String caseId;
    private String apiId;
    private String desc;
    private String params;
    private String expectResponseData;
    private String actualResponseData;
    private String preDataSql;
    private String preDataResult;
    private String afterDataSql;
    private String afterDataResult;

    public String getExpectResponseData() {
        return expectResponseData;
    }

    public void setExpectResponseData(String expectResponseData) {
        this.expectResponseData = expectResponseData;
    }

    public String getActualResponseData() {
        return actualResponseData;
    }

    public void setActualResponseData(String actualResponseData) {
        this.actualResponseData = actualResponseData;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getPreDataSql() {
        return preDataSql;
    }

    public void setPreDataSql(String preDataSql) {
        this.preDataSql = preDataSql;
    }

    public String getPreDataResult() {
        return preDataResult;
    }

    public void setPreDataResult(String preDataResult) {
        this.preDataResult = preDataResult;
    }

    public String getAfterDataSql() {
        return afterDataSql;
    }

    public void setAfterDataSql(String afterDataSql) {
        this.afterDataSql = afterDataSql;
    }

    public String getAfterDataResult() {
        return afterDataResult;
    }

    public void setAfterDataResult(String afterDataResult) {
        this.afterDataResult = afterDataResult;
    }

    @Override
    public String toString() {
        return "caseId=" + caseId + ", apiId=" + apiId +
                ", desc=" + desc + ", params=" + params +
                ", expectResponseData=" + expectResponseData +
                ", actualResponseData" + actualResponseData;
    }
}
