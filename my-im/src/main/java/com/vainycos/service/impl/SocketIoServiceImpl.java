package com.vainycos.service.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.vainycos.domain.UserDO;
import com.vainycos.service.ISocketIoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Vainycos
 * @description socker服务实现类
 * @date: 2023/7/10 9:48
 */
@Slf4j
@Service
public class SocketIoServiceImpl implements ISocketIoService {

    private final static Logger logger = LoggerFactory.getLogger(SocketIoServiceImpl.class);

    /**
     * 存放已连接的客户端
     */
    private static final Map<String, SocketIOClient> accountClientMap = new ConcurrentHashMap<>();
    private static final Map<String, UserDO> clientAccountMap = new ConcurrentHashMap<>();

    /**
     * 发送消息
     */
    private static final String SEND_MSG_EVENT = "send_msg_event";

    /**
     * 客户端接收消息
     */
    private static final String ON_RECEIVE_MSG = "on_receive_msg";

    /**
     * 未签收
     */
    private static final String UNSIGN_MSG_EVENT = "unsign_msg_event";

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 推送信息给指定客户端
     *
     * @param userId
     * @param msgContent
     */
    @Override
    public void pushMessageToAccount(String userId, Object msgContent) {
        SocketIOClient client = accountClientMap.get(userId);
        if (client != null && client.isChannelOpen()) {
            client.sendEvent(ON_RECEIVE_MSG, msgContent);
            log.info("发送人：{}，发送消息：{}", userId, msgContent);
        }
    }

    /**
     * 获取客户端url中的鉴权参数
     *
     * @param client: 客户端
     */
    private UserDO getParamsByClient(SocketIOClient client) {
        // 获取客户端url参数
        String username = client.getHandshakeData().getSingleUrlParam("username");
        String pwd = client.getHandshakeData().getSingleUrlParam("pwd");
        // 实际可以根据用户名/密码做相关校验之后通过则返回用户信息
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setUserId("vainycos");
        userDO.setUsername(username);
        userDO.setPwd(pwd);
        userDO.setNowChatUserId("nancy");
        return userDO;
    }

    /**
     * 获取连接的客户端ip地址
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        if (client.getHandshakeData().getHttpHeaders().get("x-forwarded-for") != null) {
            return client.getHandshakeData().getHttpHeaders().get("x-forwarded-for");
        }
        return sa.substring(1, sa.indexOf(":"));
    }

    /**
     * 客户端链接
     *
     * @param client 客户端
     */
    @OnConnect
    private void connectListener(SocketIOClient client) {
        UserDO user = getParamsByClient(client);
        if (null == user) {
            client.disconnect();
            return;
        }
        log.info("************ 客户端： " + getIpByClient(client) + " 已连接： " + user + " ************");
        // 这里使用用户的id作为唯一标识
        String userId = user.getUserId();
        SocketIOClient socketIOClient = accountClientMap.get(userId);
        if (null != socketIOClient) {
            if (clientAccountMap.containsKey(socketIOClient.getSessionId().toString())) {
                clientAccountMap.remove(socketIOClient.getSessionId().toString());
            }
            if (socketIOClient.isChannelOpen()) {
                socketIOClient.sendEvent("disconnect", "断开连接");
                socketIOClient.disconnect();
            }
        }
        accountClientMap.put(userId, client);
        clientAccountMap.put(client.getSessionId().toString(), user);
        logger.info("当前在线人数：" + accountClientMap.keySet().size());
        client.sendEvent("connected", "成功连接");
    }

    /**
     * 链接断开
     *
     * @param client 客户端
     */
    @OnDisconnect
    private void disconnectListener(SocketIOClient client) {
        logger.info("************ 客户端： " + getIpByClient(client) + " 已断开 ************");
        client.disconnect();
        clientAccountMap.remove(client.getSessionId().toString());
        // 这里没有删除 account，是因为存在账户后登陆，然后上一个链接才断线的情况
        logger.info("当前在线人数：" + clientAccountMap.keySet().size());
    }

    /**
     * 未签收消息监听
     *
     * @param client     客户端
     * @param data       数据
     * @param ackRequest 回调函数
     */
    @OnEvent(value = UNSIGN_MSG_EVENT)
    private void unreadConversationEventListener(SocketIOClient client, String data, AckRequest ackRequest) {
        UserDO user = clientAccountMap.get(client.getSessionId().toString());
        String userId = user.getUserId();
        if (!StringUtils.hasLength(userId)) {
            client.disconnect();
            return;
        }
        String clientIp = getIpByClient(client);
        log.info("{}.客户端 未签收消息, 消息内容.{}", clientIp, data);
        ackRequest.sendAckData("ok");
    }

    /**
     * 消息发送监听
     *
     * @param client     客户端
     * @param data       数据
     * @param ackRequest 回调函数
     */
    @OnEvent(value = SEND_MSG_EVENT)
    private void sendMsgEventListener(SocketIOClient client, Object data, AckRequest ackRequest) {
        UserDO user = clientAccountMap.get(client.getSessionId().toString());
        // 查找正在与这个用户沟通的另一位用户唯一标识，此处模拟一个不存在的用户
        String userId = user.getUserId();
        if (!StringUtils.hasLength(userId)) {
            client.disconnect();
            return;
        }
        String clientIp = getIpByClient(client);
        log.info("{}.客户端消息：{}", clientIp, data);
        pushMessageToAccount(userId, data);
        ackRequest.sendAckData(data);
    }
}
