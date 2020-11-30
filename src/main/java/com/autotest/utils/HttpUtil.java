package com.autotest.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpUtil {

    public static Map<String,String> cookies = new HashMap<>();

    /**
     * 以post方式完成接口调用
     * @param url 接口请求地址
     * @param params 接口参数
     * @return 返回请求响应
     */
    public static String doPost(String url, Map<String, String> params){
        // 指定接口请求方式
        HttpPost post = new HttpPost(url);
        // 参数集合
//        List<BasicNameValuePair> parameters = new ArrayList<>();
        String json = JSON.toJSONString(params);//map转String
        JSONObject jsonObject = JSON.parseObject(json);//String转json

        StringEntity entity = new StringEntity(jsonObject.toString(),"UTF-8");
        post.setEntity(entity);
        // 取出Map中所有的参数名
/*        Set<String> keys = params.keySet();
        for (String key:keys){
            // 以键值对形式添加参数进list
            parameters.add(new BasicNameValuePair(key, params.get(key)));
        }*/
        String result = "";
        try {
            // 以表单的形式将参数封装到请求体中
//            post.setEntity(new UrlEncodedFormEntity(parameters,"utf-8"));
            // 准备请求头数据
            post.addHeader("X-Lemonban-Media-Type","lemonban.v2");
            post.addHeader("Content-Type","application/json");
            if (url.indexOf("login") == -1){
                addCookieInRequest(post);
            }
            // 发起请求
            HttpClient client = HttpClients.createDefault();
            // 获取响应
            HttpResponse response = client.execute(post);
            // 获取状态码
//            int statusCode = response.getStatusLine().getStatusCode();
            // 获取响应报文
            result = EntityUtils.toString(response.getEntity());
            // 登录成功，将token设置进cookies
            int code = JSONObject.parseObject(result).getInteger("code");
            if (code == 0 && url.indexOf("login") != -1){
                storeResponseCookies(response, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置请求头
     * @param request 请求
     */
    private static void addCookieInRequest(HttpRequest request) {
        String jsessionId = cookies.get("JSESSIONID");
        String Authorization = cookies.get("Authorization");
        if (jsessionId != null){
            request.addHeader("Cookie",jsessionId);
        }
        if (Authorization != null){
            request.addHeader("Authorization",Authorization);
        }
    }

    /**
     * 从响应头中获取相关数据
     * @param response 响应
     * @param result 响应结果
     */
    private static void storeResponseCookies(HttpResponse response,String result) {
        // 从响应头中取出“Set-Cookie”的响应头
        Header header = response.getFirstHeader("Set-Cookie");
        if (header != null) {
            String cookiePairString = header.getValue();
            if (cookiePairString != null && cookiePairString.trim().length() > 0) {
                // 以分号分割
                String[] cookiePairs = cookiePairString.split(";");
                if (cookiePairs != null) {
                    for (String cookiePair : cookiePairs) {
                        if (cookiePair.contains("JSESSIONID")) {
                            cookies.put("JSESSIONID", cookiePair);
                        }
                    }
                }
            }
        }

        JSONObject data = JSONObject.parseObject(result).getJSONObject("data").getJSONObject("token_info");
        cookies.put("Authorization", "Bearer " + data.getString("token"));
        System.out.println("cookies:" + cookies);
    }

    /**
     * 以get方式完成接口调用
     * @param url 接口请求地址
     * @param params 请求参数
     * @return 返回请求响应
     */
    public static String doGet(String url, Map<String, String> params){

        Set<String> keys = params.keySet();
        for (String key : keys) {
            url += ("&" + key + "=" + params.get(key));
        }
        System.out.println(url);
        // 指定接口请求方式
        HttpGet get = new HttpGet(url);

        String result = "";
        try {
            addCookieInRequest(get);
            // 发起请求
            HttpClient client = HttpClients.createDefault();
            // 获取响应
            HttpResponse response = client.execute(get);
            // 获取响应信息
//            int statusCode = response.getStatusLine().getStatusCode();
            // 获取响应报文
            result = EntityUtils.toString(response.getEntity());
            // 登录成功，将token设置进cookies
            int code = JSONObject.parseObject(result).getInteger("code");
            if (code == 0 && url.indexOf("login") != -1){
                storeResponseCookies(response, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据不同类型调用不同方法
     * @param url 接口地址
     * @param methodType 方法类型
     * @param parameter 参数
     * @return 返回响应结果
     */
    public static String doMethod(String url, String methodType, Map<String, String> parameter){
        String result = "";
        if ("post".equalsIgnoreCase(methodType)){
            result = HttpUtil.doPost(url, parameter);
        }else if("get".equalsIgnoreCase(methodType)){
            result = HttpUtil.doGet(url, parameter);
        }
        return result;
    }
}
