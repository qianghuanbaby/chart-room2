package com.qh.vo;

import lombok.Data;

/**
 * Author:qh
 * Created:2019/8/24
 * 服务器与客户端传递信息载体
 */

@Data
public class MessageVo {
    /**
     * 表示告知服务器要进行的动作，1表示用户注册，2表示私聊
     */
    private String type;
    /**
     * 发送到服务器的具体内容
     */
    private String content;
    /**
     * 私聊告知服务器，要将信息发给那个用户
     */
    private String to;
}
