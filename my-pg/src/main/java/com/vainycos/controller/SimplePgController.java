package com.vainycos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Vainycos
 * @description pg
 * @date: 2023/6/25 14:21
 */
@Slf4j
@RequestMapping("/pg")
@RestController
public class SimplePgController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/query")
    public Long query() {
        Long count = jdbcTemplate.queryForObject("select count(*) from test", Long.class);
        log.info("记录总数：{}", count);
        return count;
    }
}
