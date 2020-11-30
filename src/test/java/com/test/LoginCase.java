package com.test;

import com.autotest.base.BaseProcessor;
import com.autotest.utils.CaseUtil;
import org.testng.annotations.DataProvider;

public class LoginCase extends BaseProcessor {

    @DataProvider(name = "data")
    public Object[][] login(){
        return CaseUtil.getCaseDatasByApiId("3",super.cellNames);
    }
}
