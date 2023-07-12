package com.vainycos.consumer.service.impl;

import com.vainycos.api.api_interface.OrderService;
import com.vainycos.api.model.Order;
import com.vainycos.consumer.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/8/4 15:57
 */
// Spring的Service注解
@Service
public class UserServiceImpl implements UserService {

    @Reference
    private OrderService orderService;


    @Override
    public List<Order> findOrdersByUserId(Integer id) {
        return orderService.findOrdersByUserId(id);
    }
}
