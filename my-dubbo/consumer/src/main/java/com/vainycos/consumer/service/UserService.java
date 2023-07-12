package com.vainycos.consumer.service;

import com.vainycos.api.model.Order;

import java.util.List;

public interface UserService {

    List<Order> findOrdersByUserId(Integer id);
}
