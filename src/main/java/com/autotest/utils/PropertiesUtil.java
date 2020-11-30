package com.autotest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties properties = new Properties();

    static{
        try {
            InputStream inputStream = new FileInputStream(new File("src/main/resources/config.properties"));
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件的excel路径
     * @return 返回excel文件路径
     */
    public static String getExcelPath(){
        return properties.getProperty("excel.path");
    }
}
