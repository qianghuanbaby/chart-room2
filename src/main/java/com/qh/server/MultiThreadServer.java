package com.qh.server;

import com.qh.util.CommUtils;
import com.qh.vo.MessageVo;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:qh
 * Created:2019/8/24
 * 聊天室服务端
 */

public class MultiThreadServer {
    private static final String IP;
    private static final int PORT;
    //缓存当前服务器所有在线的客户端信息，key值就是用户名
    private static Map<String, Socket> clients = new ConcurrentHashMap<>();
    //缓存当前服务器注册的所有群名称以及群好友
    private static Map<String,Set<String>> groups = new ConcurrentHashMap<>();
    static {
        Properties pros = CommUtils.loadProperties("socket.properties");  //获取配置文件
        IP= pros.getProperty("address");
        PORT = Integer.parseInt(pros.getProperty("port"));
    }

    //处理每个线程连接的内部类
    private static class ExecuteClient implements Runnable{
        private Socket client;
        private Scanner in;
        private PrintStream out;

        public ExecuteClient(Socket client){
            this.client = client;
            try {
                this.in = new Scanner(client.getInputStream());
                this.out = new PrintStream(client.getOutputStream(),true,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true){
                if(in.hasNextLine()){
                    String jsonStrFromClient = in.nextLine();
                    MessageVo msgFromClient = (MessageVo) CommUtils.json2Object
                            (jsonStrFromClient,MessageVo.class);
                    if(msgFromClient.getType().equals("1")){
                        //新用户注册到服务端
                        String userName = msgFromClient.getContent();
                        //将当前在线的所有用户的用户名发回客户端
                        MessageVo msg2Client = new MessageVo();
                        msg2Client.setType("1");
                        msg2Client.setContent(CommUtils.object2Json(clients.keySet()));
                        out.println(CommUtils.object2Json(msg2Client));
                        //将新上线的用户信息发回给当前已在线的所有用户
                        sendUserLogin("newLogin:"+userName);
                        //将当前新用户注册到服务端缓存
                        clients.put(userName,client);
                        System.out.println(userName+"上线了!");
                        System.out.println("当前聊天室共有"+clients.size()+"人");
                    }else if(msgFromClient.getType().equals("2")){
                        //用户私聊
                        /**
                         * type:2
                         * Content:myName-msg
                         * to:friendName
                         */

                        String friendName = msgFromClient.getTo();
                        Socket clientSocket = clients.get(friendName);
                        try {
                            PrintStream out = new PrintStream(clientSocket.getOutputStream(),true
                            ,"UTF-8");
                            MessageVo msg2Client = new MessageVo();
                            msg2Client.setType("2");
                            msg2Client.setContent(msgFromClient.getContent());
                            System.out.println("收到私聊信息，内容为"+msgFromClient.getContent());
                            out.println(CommUtils.object2Json(msg2Client));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(msgFromClient.getType().equals("3")){
                        //注册群
                        String groupName = msgFromClient.getContent();
                        //该群的所有成员
                        Set<String> friends = (Set<String>) CommUtils.json2Object(msgFromClient.getTo(),Set.class);
                        groups.put(groupName,friends);
                        System.out.println("有新的群注册成功，群名称为"+groupName+"一共有"+groups.size()+"个群");
                    }else if(msgFromClient.getType().equals("4")){
                        //群聊信息
                        System.out.println("服务器收到的群聊信息为:"+msgFromClient);
                        String groupName = msgFromClient.getTo();
                        Set<String> names = groups.get(groupName);
                        Iterator<String> iterator = names.iterator();
                        while (iterator.hasNext()){
                            String socketName = iterator.next();
                            Socket client = clients.get(socketName);
                            try {
                                PrintStream out = new PrintStream(client.getOutputStream(),true,"UTF-8");
                                MessageVo messageVo = new MessageVo();
                                messageVo.setType("4");
                                messageVo.setContent(msgFromClient.getContent());
                                //群名+好友列表
                                messageVo.setTo(groupName+"-"+CommUtils.object2Json(names));
                                out.println(CommUtils.object2Json(messageVo));
                                System.out.println("服务端发送的群聊信息为:"+messageVo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        /**
         * 向所有在线用户发送新用户上线信息
         * @param msg
         */
        private void sendUserLogin(String msg){
            for(Map.Entry<String,Socket> entry:clients.entrySet()){
                Socket socket = entry.getValue();  //取出当前实体的每一个Socket
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                    out.println(msg);  //将当前用户上线信息发给所有用户
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);   //本地建立基站
        ExecutorService executors = Executors.newFixedThreadPool(50);  //创建线程池
        for(int i = 0; i<50; i++){
            System.out.println("等待客户端连接...");
            Socket client = serverSocket.accept();
            System.out.println("有新的连接,端口号为"+client.getPort());
            executors.submit(new ExecuteClient(client));
        }
    }
}
