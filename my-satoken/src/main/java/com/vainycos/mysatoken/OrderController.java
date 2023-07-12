package com.vainycos.mysatoken;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/12/19 14:30
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @SaCheckPermission("orders")
    @GetMapping("/test")
    public String test(){
        return "orders权限标识允许访问";
    }
}
