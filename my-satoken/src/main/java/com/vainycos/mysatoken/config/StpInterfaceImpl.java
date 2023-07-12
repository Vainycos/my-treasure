package com.vainycos.mysatoken.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/12/19 14:47
 */
@Component
public class StpInterfaceImpl implements StpInterface {


    /**
     * 返回一个账号所拥有的权限码集合
      * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // loginId="1" 则拥有order权限标识，否则没有
        if ("1".equals(loginId)){
            return Arrays.asList("orders");
        }else {
            return Arrays.asList("default");
        }
    }

    /**
     * 返回一个账号所拥有的角色标识集合
      * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // loginId="666" 表示管理员，否则非管理员
        if ("666".equals(loginId)){
            return Arrays.asList("admin");
        }else {
            return Arrays.asList("general");
        }
    }
}
