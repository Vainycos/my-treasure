package com.vainycos.provider.service;

import com.vainycos.api.api_interface.OrderService;
import com.vainycos.api.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/8/4 15:29
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> findOrdersByUserId(Integer userId) {
        // do something
        log.info("获取订单消息...");
        Order order = new Order();
        order.setId(1L);
        order.setUserId(userId);
        List<Order> list = new ArrayList<>();
        list.add(order);
        return list;
    }
}
