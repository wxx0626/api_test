package com.test;

import com.autotest.base.BaseProcessor;
import com.autotest.utils.CaseUtil;
import org.testng.annotations.DataProvider;

public class RechargeCase extends BaseProcessor {

    @DataProvider(name = "data")
    public Object[][] recharge(){
        return CaseUtil.getCaseDatasByApiId("4",super.cellNames);
    }
}
