package com.qh.client.service;

import com.qh.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author:qh
 * Created:2019/8/24
 */

public class Connect2Server {
    private static final String IP;
    private static final int PORT;
    static {
        Properties pros = CommUtils.loadProperties("socket.properties");  //获取配置文件
        IP= pros.getProperty("address");
        PORT = Integer.parseInt(pros.getProperty("port"));
    }

    private Socket client;
    private InputStream in;
    private OutputStream out;

    public Connect2Server(){
        try {
            client = new Socket(IP,PORT);
            in = client.getInputStream();
            out  = client.getOutputStream();
        } catch (IOException e) {
            System.out.println("与服务器建立连接失败");
            e.printStackTrace();
        }
    }

    //主要获取建立连接后的输入输出流(传递数据)
    public InputStream getIn(){
        return in;
    }

    public OutputStream getOut(){
        return out;
    }
}
