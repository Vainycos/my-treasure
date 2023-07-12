package com.vainycos.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/8/4 15:24
 */
@Data
public class Order implements Serializable {
    private Long id;
    private Date createTime;
    private Integer userId;
    private List<OrderItem> orderItemList;
}

