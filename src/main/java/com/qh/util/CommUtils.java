package com.qh.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author:qh
 * Created:2019/8/23
 * 封装公共工具方法，如加载配置文件、json序列化等
 */

public class CommUtils {
    private static final Gson GSON = new GsonBuilder().create();

    //crtl+shift+t ,放到方法名上
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        //通过类加载器获取文件输入流
        InputStream in = CommUtils.class.getClassLoader().getResourceAsStream(fileName);
        try{
            properties.load(in);
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    //将任意对象序列化为json字符串
    public static String object2Json(Object obj){
        return GSON.toJson(obj);
    }

    /**
     * //将任意对象反序列化为对象
     * @param jsonStr json字符串
     * @param objClass 反序列化的类反射对象
     * @return
     */
    public static Object json2Object(String jsonStr, Class objClass){
        return GSON.fromJson(jsonStr,objClass);
    }
}
