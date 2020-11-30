package com.autotest.base;

import com.alibaba.fastjson.JSONObject;
import com.autotest.utils.*;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.Map;

public class BaseProcessor {

    private Logger logger = Logger.getLogger(BaseProcessor.class);
    public String[] cellNames = {"CaseId", "ApiId", "Params","ExpectResponseData", "PreDataSql", "AfterDataSql"};

    @Test(dataProvider = "data")
    public void test(String caseId, String apiId, String params, String expectResult, String preDataSql, String afterDataSql){
        logger.info("开始前的接口数据验证");
        if (preDataSql != null && preDataSql.trim().length() >1){
            // 替换sql中的所有变量
            preDataSql = VariableUtil.replaceVariables(preDataSql);
            System.out.println("preDataSql:" + preDataSql);
            String preDtaResult = DBCheckUtil.doQuery(preDataSql);
            ExcelUtil.writeBackDataList.add(new WriteBackData("case", caseId, "PreDataResult", preDtaResult));
        }
        String url = RestUtil.getUrlByApiId(apiId);
        String methodType = RestUtil.getMethodTypeByApiId(apiId);
        // 替换测试数据中的所有变量
        params = VariableUtil.replaceVariables(params);
        System.out.println("params:" + params);
        // 将参数解析到map中
        Map<String, String> parameter = (Map<String, String>) JSONObject.parse(params);
        String actualResult = HttpUtil.doMethod(url, methodType, parameter);
        System.out.println(actualResult);

        String assertResult = AssertUtil.getResult(expectResult,actualResult);
        // 保存回写数据对象
        ExcelUtil.writeBackDataList.add(new WriteBackData("case", caseId, "ActualResponseData", assertResult));
        logger.info("接口调用后的数据验证");
        if (afterDataSql != null && afterDataSql.trim().length() >1){
            // 替换sql中的所有变量
            afterDataSql = VariableUtil.replaceVariables(afterDataSql);
            System.out.println("afterDataSql:" + afterDataSql);
            String afterDtaResult = DBCheckUtil.doQuery(afterDataSql);
            ExcelUtil.writeBackDataList.add(new WriteBackData("case", caseId, "AfterDataResult", afterDtaResult));
        }
    }

    @AfterSuite
    public void batchWriteBackData(){
        ExcelUtil.batchWriteBackData(PropertiesUtil.getExcelPath());
    }
}
