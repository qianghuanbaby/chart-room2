package com.qh.client.service;

import com.qh.client.dao.AccountDao;
import com.qh.client.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author:qh
 * Created:2019/8/24
 */

public class UserReg {
    private JPanel userRegPanel;
    private JTextField userNameText;
    private JPasswordField passwordText;
    private JTextField briefText;
    private JButton regBtn;
    AccountDao accountDao = new AccountDao();

    public UserReg(){
        JFrame frame = new JFrame("用户注册");
        frame.setContentPane(userRegPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        //点击注册按钮，将信息持久化到db中，成功会弹出提示框
        regBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户输入的注册信息
                String userName = userNameText.getText();  //获取当前输入的内容
                String password = String.valueOf(passwordText.getPassword());
                String brief = briefText.getText();
                //将输入信息包装成User类，保存到数据库中
                User user = new User();
                user.setUserName(userName);
                user.setPassword(password);
                user.setBrief(brief);
                //调用dao
                if(accountDao.userReg(user)){
                    //弹出提示框：注册成功，返回登录页面
                    JOptionPane.showMessageDialog(frame,"注册成功！",
                            "提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);  //把当前注册页面置为不可见
                }else{
                    //弹出提示框：注册失败，保留当前注册页面，可以继续注册
                    JOptionPane.showMessageDialog(frame,"注册失败！",
                            "错误信息",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
