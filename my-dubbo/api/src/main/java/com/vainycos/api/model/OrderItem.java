package com.vainycos.api.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/8/4 15:24
 */
@Data
public class OrderItem {

    private Long id;
    private Integer goodsId;

    /**
     * 商品价格可能变动，需要记录购买时的价格
     */
    private BigDecimal price;
    private Integer number;
    /**
     * 所属order
     */
    private Long orderId;
}
