package com.vainycos.mysatoken;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/12/19 14:30
 */
@SaCheckRole({"admin", "my-role"})
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public String test(){
        return "admin角色允许访问";
    }

    @GetMapping("/testAnno")
    public String testAnno(){
        return "注解方式实现角色放通";
    }
}
