package com.qh.client.service;

import com.qh.util.CommUtils;
import com.qh.vo.MessageVo;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Author:qh
 * Created:2019/8/25
 */

public class PrivateChatGUI {
    private JPanel privatePanel;
    private JTextArea readFromServer;
    private JTextField send2Server;
    private JFrame frame;
    private PrintStream out;

    private String friendName;
    private String myName;
    private Connect2Server connect2Server;

    public PrivateChatGUI(String friendName,String myName,Connect2Server connect2Server){
        this.friendName = friendName;
        this.myName = myName;
        this.connect2Server = connect2Server;
        try {
            this.out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        frame = new JFrame("与"+friendName+"私聊中...");
        frame.setContentPane(privatePanel);

        //设置窗口关闭的操作，将其设置为隐藏
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);

        //捕捉输入框的键盘输入
        send2Server.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append(send2Server.getText());
                //1. 当捕捉到按下Enter
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    //2. 将当前信息发送到服务端
                    String msg = sb.toString();
                    MessageVo messageVo = new MessageVo();
                    messageVo.setType("2");
                    messageVo.setContent(myName+"-"+msg);
                    messageVo.setTo(friendName);
                    PrivateChatGUI.this.out.println(CommUtils.object2Json(messageVo));
                    //3 .自己发送的信息展示到当前私聊界面
                    readFromServer(myName+"说:"+msg);
                    send2Server.setText("");
                }

            }
        });
    }

    public void readFromServer(String msg){
        readFromServer.append(msg+"\n");
    }
    public JFrame getFrame(){
        return frame;
    }
}
