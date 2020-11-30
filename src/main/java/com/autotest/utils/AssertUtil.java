package com.autotest.utils;

import org.testng.Assert;

public class AssertUtil {

    /**
     * 返回断言结果
     * @param expectResult 预期结果
     * @param ActualResult 实际结果
     * @return 返回断言结果
     */
    public static String getResult(String expectResult, String ActualResult){
        // 断言结果
        String assertResult = "通过";
        try {
            Assert.assertEquals(expectResult,ActualResult);
        }
        catch (AssertionError ae) {
            assertResult = ActualResult;
        }
        return assertResult;
    }
}
