package com.test;

import org.apache.http.client.methods.HttpPost;

import java.util.*;

import static com.autotest.utils.HttpUtil.doPost;

public class PostDemo {

    public static void main(String[] args) {
        // 接口地址
        String url = "http://hd215.api.yesapi.cn/?s=App.User.Login";
        // 准备测试数据
        String app_key = "941EADA3DF57BE2DFB0920398B6466D6";
        String username = "2538699146";
        String password = "db2ba1a294c3130b3b2ae755985442aa";
        // 参数集合
        Map<String, String> params = new HashMap<>();
        // 以键值对形式添加参数进list
        params.put("app_key",app_key);
        params.put("username",username);
        params.put("password",password);

        String result = doPost(url, params);
        System.out.println(result);
    }
}
