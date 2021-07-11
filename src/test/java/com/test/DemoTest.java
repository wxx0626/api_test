package com.test;

import org.testng.annotations.*;

/**
 * @Description:
 * @Author: xiangxiang.wu
 * @Date: 2021/6/1 16:43
 */
public class DemoTest {

    @BeforeSuite
    public void beforeSuite(){
        System.out.print("BeforeSuite");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.print("AfterSuite");
    }

    @BeforeClass
    public void beforeClass(){
        System.out.print("BeforeClass");
    }

    @AfterClass
    public void afterClass(){
        System.out.print("AfterClass");
    }

    @BeforeGroups(groups = {"groups1"})
    public void beforeGroups1(){
        System.out.print("BeforeGroups1");
    }

    @AfterGroups(groups = {"groups1"})
    public void afterGroups1(){
        System.out.print("AfterGroups1");
    }

    @BeforeGroups(groups = {"groups2"})
    public void beforeGroups2(){
        System.out.print("BeforeGroups2");
    }

    @AfterGroups(groups = {"groups2"})
    public void afterGroups2(){
        System.out.print("AfterGroups2");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.print("BeforeMethod");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.print("AfterMethod");
    }

    @BeforeTest
    public void beforeTest(){
        System.out.print("BeforeTest");
    }

    @AfterMethod
    public void afterTest(){
        System.out.print("AfterTest");
    }

    @Test
    public void test1(){
        System.out.print("test1正常");
    }

    @Test(groups = {"groups1"})
    public void test2(){
        System.out.print("test2分组1");
    }

    @Test(groups = {"groups2"})
    public void test3(){
        System.out.print("test3分组2");
    }

    @Test(groups = {"groups1","groups2"})
    public void test4(){
        System.out.print("test4分组1,2");
    }

    @Test(enabled = false)
    public void test5(){
        System.out.print("test5忽略测试");
    }

    @Test(alwaysRun = true)
    public void test6(){
        System.out.print("test6一直运行");
    }

    @Test(testName = "test7")
    public void test7(){
        System.out.print("test7方法名");
        throw new RuntimeException();
    }

    @Test(dependsOnMethods = "test7")
    public void test8(){
        System.out.print("test6依赖测试7");
    }

    @Test(priority = 2)
    public void test9(){
        System.out.print("test9优先级2");
    }

    @Test(priority = 1)
    public void test10(){
        System.out.print("test10优先级1");
    }

    @Test(groups = "group11")
    public void test11(){
        System.out.println("test11分组1");
    }
}
