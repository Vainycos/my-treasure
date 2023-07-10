package com.vainycos.controller;

import com.alibaba.fastjson2.JSONObject;
import com.vainycos.service.ISocketIoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Vainycos
 * @description im控制器
 * @date: 2023/7/10 14:45
 */
@RestController
@RequestMapping("/im")
public class IMController {

    @Resource
    private ISocketIoService socketIoService;

    /**
     * 主动发送消息
     * @param userId
     * @param msgContent
     * @return
     */
    @GetMapping("/sendMessage")
    public Boolean sendMessageToClient(String userId, String msgContent){
        JSONObject msgJson = new JSONObject();
        msgJson.put("userName", userId);
        msgJson.put("message", msgContent);
        socketIoService.pushMessageToAccount(userId, msgJson);
        return true;
    }
}
