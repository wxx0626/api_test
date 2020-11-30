package com.test;

import com.autotest.base.BaseProcessor;
import com.autotest.utils.CaseUtil;
import org.testng.annotations.DataProvider;

public class RegisterCase extends BaseProcessor {

    @DataProvider(name = "data")
    public Object[][] register(){
        return CaseUtil.getCaseDatasByApiId("2",super.cellNames);
    }
}
