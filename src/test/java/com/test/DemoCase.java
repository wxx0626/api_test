package com.test;

import com.alibaba.fastjson.JSONObject;
import com.autotest.base.BaseProcessor;
import com.autotest.utils.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class DemoCase extends BaseProcessor {

    @Test(dataProvider = "loginCase0",enabled = false)
    public void test0(String app_key, String username, String password){
        // 接口地址
        String url = "http://hd215.api.yesapi.cn/?s=App.User.Login";

        // 参数集合
        Map<String, String> params = new HashMap<>();
        // 以键值对形式添加参数进list
        params.put("app_key",app_key);
        params.put("username",username);
        params.put("password",password);

        System.out.println(HttpUtil.doPost(url, params));
    }

    @Test(dataProvider = "loginCase1",enabled = false)
    public void test1(String params){
        // 接口地址
        String url = "http://hd215.api.yesapi.cn/?s=App.User.Login";

        // 参数集合
        Map<String, String> parameter = (Map<String, String>) JSONObject.parse(params);
        System.out.println(HttpUtil.doPost(url, parameter));
    }

    @Test(dataProvider = "loginCase2",enabled = false)
    public void test2(String apiIdFromCase,String params){
        int[] rows = {2};
        int[] cells = {1,3,4};
        // 接口地址
        String url = "";
        String methodType = "";
        Object[][] datas = ExcelUtil.datas("src/main/resources/case_02.xlsx", "interface", rows, cells);
        for (Object[] data : datas) {
            String interfaceId = data[0].toString();
            // 比对接口id，获取相应的接口地址和访问方法类型
            if (apiIdFromCase.equals(interfaceId)){
                url = data[1].toString();
                methodType = data[2].toString();
                break;
            }
        }
        // 参数集合
        Map<String, String> parameter = (Map<String, String>) JSONObject.parse(params);
        System.out.println(HttpUtil.doMethod(url, methodType, parameter));
    }

    @Test(dataProvider = "loginCase3",enabled = false)
    public void test3(String caseId, String apiId, String params, String expectResult){
        String url = RestUtil.getUrlByApiId(apiId);
        String methodType = RestUtil.getMethodTypeByApiId(apiId);
        // 参数集合
        Map<String, String> parameter = (Map<String, String>) JSONObject.parse(params);
        String result = HttpUtil.doMethod(url, methodType, parameter);
        System.out.println(result);

        // 回写
        ExcelUtil.writeBackData("src/main/resources/case_04.xlsx", "case", caseId, "ActualResponseData", result);
    }

    @DataProvider(name = "login")
    public Object[][] datas(){
        return new Object[][]{
                {"941EADA3DF57BE2DFB0920398B6466D6", "2538699146", "db2ba1a294c3130b3b2ae755985442aa"},
                {"941EADA3DF57BE2DFB0920398B6466D6", "2538699147", "db2ba1a294c3130b3b2ae755985442aa"},
                {"941EADA3DF57BE2DFB0920398B6466D6", "2538699146", "db2ba1a294c3130b3b2ae755985442ab"},
                {"941EADA3DF57BE2DFB0920398B6466D5", "2538699146", "db2ba1a294c3130b3b2ae755985442aa"}
        };
    }

    @DataProvider(name = "data")
    public Object[][] data(){
        String[] cellNames = {"CaseId", "ApiId", "Params","ExpectResponseData"};
        return CaseUtil.getCaseDatasByApiId("1",cellNames);
    }

    /**
     * 连续的用例参数
     * @return 返回获取的用例数据
     */
    @DataProvider(name = "loginCase0")
    public Object[][] loginCase0(){
        return ExcelUtil.continuousDatas("src/main/resources/case_00.xlsx", 2,5,6,8);
    }

    /**
     * 不连续的用例参数
     * @return 返回获取的用例数据
     */
    @DataProvider(name = "loginCase1")
    public Object[][] loginCase1(){
        int[] rows = {2,3,4,5};
        int[] cells = {6};
        return ExcelUtil.discreteDatas("src/main/resources/case_01.xlsx", rows, cells);
    }

    /**
     * 带sheet页的用例参数
     * @return 返回获取的用例数据
     */
    @DataProvider(name = "loginCase2")
    public Object[][] loginCase2(){
        int[] rows = {2,3,4,5};
        int[] cells = {3,4};
        return ExcelUtil.datas("src/main/resources/case_02.xlsx", "loginCase", rows, cells);
    }

    /**
     * 根据接口编号获取对应接口的用例参数数据
     * @return 返回获取的用例数据
     */
    @DataProvider(name = "loginCase3")
    public Object[][] loginCase3(){
        String[] cellNames = {"CaseId", "ApiId", "Params","ExpectResponseData"};
        return CaseUtil.getCaseDatasByApiId("1",cellNames);
    }

    public static void main(String[] args) {
        String params = "{\"app_key\":\"941EADA3DF57BE2DFB0920398B6466D6\",\"username\":\"2538699146\",\"password\":\"db2ba1a294c3130b3b2ae755985442aa\"}";
        // 参数集合
        DemoParams demoParams = JSONObject.parseObject(params, DemoParams.class);
        System.out.println(demoParams);
    }
}

class DemoParams{
    private String app_key;
    private String username;
    private  String password;

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "app_key=" + app_key + ", username=" + username + ", password=" + password;
    }
}
