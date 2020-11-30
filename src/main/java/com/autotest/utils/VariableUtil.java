package com.autotest.utils;

import java.lang.reflect.Method;
import java.util.*;

public class VariableUtil {

    public static Map<String,String> variableNameAndValue = new HashMap<>();
    public static List<Variable> variables = new ArrayList<>();
    static{
        List<Variable> list = ExcelUtil.load(PropertiesUtil.getExcelPath(),"variable",Variable.class);
        variables.addAll(list);
        // 将变量名和变量值放入map中
        loadVariableToMap();
        // 行列映射，为回写做准备
        ExcelUtil.loadRowNumAndCellNumMapping(PropertiesUtil.getExcelPath(), "variable");
    }

    /**
     * 将变量名和变量值保存到map中
     */
    private static void loadVariableToMap() {
        for (Variable variable : variables) {
            String variableName = variable.getName();
            String variableValue = variable.getValue();
            // 如果值为空
            if (variableValue == null || variableValue.trim().length() == 0){
                String reflectClass = variable.getReflectClass();
                String reflectMethod = variable.getReflectMethod();
                try {
                    // Class.forName根据全路径反射得到类型的字节码
                    Class clazz = Class.forName(reflectClass);
                    // 通过反射创建对象
                    Object object = clazz.newInstance();
                    // 获取反射调用的方法对象
                    Method method = clazz.getMethod(reflectMethod);
                    // 反射嗲用方法，获取方法的返回值
                    variableValue = (String) method.invoke(object);
                    // 保存要回写的数据到集合
                    ExcelUtil.writeBackDataList.add(new WriteBackData("variable",variableName,"ReflectValue",variableValue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            variableNameAndValue.put(variableName, variableValue);
        }
    }

    /**
     * 替换参数中的变量值
     * @param parameter 参数
     * @return 替换后的参数
     */
    public static String replaceVariables(String parameter){
        // 取出所有变量名
        Set<String> variableNames = variableNameAndValue.keySet();
        // 遍历变量名
        for (String variableName : variableNames) {
            String variableValue = variableNameAndValue.get(variableName);
            // 判断参数中是否出现变量名，出现变量名时需替换
            if (parameter.contains(variableName)){
                // 备注：不能使用replaceAll，因为replaceAll第一个变量是正则，而$是正则的结束符
                parameter = parameter.replace(variableName, variableValue);
            }
        }
        return parameter;
    }
}

class Variable {
    private String name;
    private String value;
    private String remark;
    private String ReflectClass;
    private String ReflectMethod;
    private String ReflectValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public String getReflectClass() {
        return ReflectClass;
    }

    public void setReflectClass(String reflectClass) {
        ReflectClass = reflectClass;
    }

    public String getReflectMethod() {
        return ReflectMethod;
    }

    public void setReflectMethod(String reflectMethod) {
        ReflectMethod = reflectMethod;
    }

    public String getReflectValue() {
        return ReflectValue;
    }

    public void setReflectValue(String reflectValue) {
        ReflectValue = reflectValue;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
