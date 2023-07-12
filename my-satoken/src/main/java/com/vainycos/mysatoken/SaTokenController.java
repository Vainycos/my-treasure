package com.vainycos.mysatoken;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.*;

/**
 * @author: Vainycos
 * @description sa-token
 * @date: 2022/12/13 16:34
 */
@RestController
public class SaTokenController {

    @GetMapping("/login")
    private String login(String usId) {
        StpUtil.login(usId);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return usId;
    }

    @GetMapping("/getName")
    private String getName(String name){
        return name;
    }

}
