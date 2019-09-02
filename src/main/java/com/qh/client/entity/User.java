package com.qh.client.entity;

import lombok.Data;

import java.util.Set;

/**
 * Author:qh
 * Created:2019/8/24
 * 实体类，用户操作的
 */
@Data
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String brief;
    private Set<String> userNames;
}
