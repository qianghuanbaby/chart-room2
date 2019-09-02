package com.qh;

import com.qh.util.CommUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

/**
 * Author:qh
 * Created:2019/8/23
 */

public class CommUtilsTest {

    //注解@Test 表示单元测试
    @Test
    public void loadProperties() {
        String fileName = "datasource.properties";
        Properties properties = CommUtils.loadProperties(fileName);
        //1.System.out.println(properties);
        //2.使用断言
        Assert.assertNotNull(properties);
    }
}