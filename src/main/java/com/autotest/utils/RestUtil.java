package com.autotest.utils;

import java.util.ArrayList;
import java.util.List;

public class RestUtil {
    // 保存所有的接口信息对象（共享数据）
    public static List<RestParam> restParams = new ArrayList<>();

    // 将所有数据解析封装到restParams
    static{
        restParams = ExcelUtil.load(PropertiesUtil.getExcelPath(), "interface", RestParam.class);
    }

    /**
     * 根据接口编号获取接口地址
     * @param apiId 接口编号
     * @return 返回接口地址
     */
    public static String getUrlByApiId(String apiId){

        for (RestParam restParam : restParams) {
            if (restParam.getInterfaceId().equals(apiId)){
                return restParam.getUrl();
            }
        }
        return "";
    }

    /**
     * 根据接口编号获取请求方式
     * @param apiId 接口编号
     * @return 返回请求方式
     */
    public static String getMethodTypeByApiId(String apiId){
        for (RestParam restParam : restParams) {
            if (restParam.getInterfaceId().equals(apiId)){
                return restParam.getRequestType();
            }
        }
        return "";
    }

}

class RestParam{
    private String InterfaceId;
    private String name;
    private String url;
    private String requestType;

    public String getInterfaceId() {
        return InterfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        InterfaceId = interfaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}