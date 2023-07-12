package com.vainycos.api.api_interface;

import com.vainycos.api.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> findOrdersByUserId(Integer userId);
}
