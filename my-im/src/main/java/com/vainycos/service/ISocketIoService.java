package com.vainycos.service;

public interface ISocketIoService {

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 推送客户端消息
     * @param userId 用户唯一标识
     * @param msgContent 消息内容
     */
    void pushMessageToAccount(String userId, Object msgContent);
}
