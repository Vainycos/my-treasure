package com.vainycos.consumer.controller;

import com.vainycos.api.model.Order;
import com.vainycos.consumer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/8/4 16:22
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public List<Order> getUser(Integer userId){
        List<Order> ordersByUserId = userService.findOrdersByUserId(userId);
        return ordersByUserId;
    }
}
