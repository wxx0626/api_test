package com.autotest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JDBCUtil {

    public static Properties properties = new Properties();
    static{
        try {
            InputStream inputStream = new FileInputStream(new File("src/main/resources/jdbc.properties"));
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据sql查询表数据，map形式返回
     * @param sql 要执行的查询语句
     * @param values 条件字段的值
     * @return 返回查询的结果（字段名和字段值的映射map键值对）
     */
    public static Map<String,Object> query(String sql,Object ... values){
        Map<String, Object> columnLabelAndValues = null;
        try {
            // 根据连接信息，获得数据库连接
            Connection connection = getConnection();
            // 获取PreparedStatement对象（提供数据库操作方法）
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // 设置条件字段值（实时绑定）
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i+1, values[i]);
            }
            // 调用查询方法，执行查询返回ResultSet（结果集）
            ResultSet resultSet = preparedStatement.executeQuery();
            // 获取查询相关信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 查询字段数目
            int columnCount = metaData.getColumnCount();

            // 结果集中取查询数据
            columnLabelAndValues = new HashMap<>();
            while (resultSet.next()){
                // 循环取出每个查询字段的数据
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    String columnValue = resultSet.getObject(columnLabel).toString();
                    columnLabelAndValues.put(columnLabel, columnValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnLabelAndValues;
    }

    /**
     * 获取数据库连接
     * @return 返回连接结果
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        // 从properties获取url
        String url = properties.getProperty("jdbc.url");
        // 从properties获取user和password
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

}
