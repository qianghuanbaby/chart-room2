package com.qh.util;

import com.qh.client.entity.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Author:qh
 * Created:2019/8/24
 */

public class CommUtilsTest {

    @Test
    public void object2Json() {
        User user = new User();
        user.setId(1);
        user.setUserName("test");
        user.setPassword("123");
        user.setBrief("帅");
        Set<String> strings = new HashSet<>();
        strings.add("test2");
        strings.add("test3");
        strings.add("test4");
        user.setUserNames(strings);
        String str = CommUtils.object2Json(user);
        System.out.println(str);
    }

    @Test
    public void json2Object() {
        String jsonStr = "{\"id\":1,\"userName\":\"test\",\"password\":\"123\",\"brief\":\"帅\",\"userNames\":[\"test4\",\"test2\",\"test3\"]}";
        User user = (User) CommUtils.json2Object(jsonStr,User.class);
        System.out.println(user.getUserNames());
    }
}