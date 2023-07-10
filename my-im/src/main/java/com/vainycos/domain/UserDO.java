package com.vainycos.domain;

import lombok.Data;

/**
 * @author: Vainycos
 * @description 用户信息
 * @date: 2023/7/10 14:27
 */
@Data
public class UserDO {

    /**
     * id主键
     */
    private Long id;

    /**
     * 用户唯一标识
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 与当前用户沟通的另一位用户唯一标识
     */
    private String nowChatUserId;
}
