package com.vainycos.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: Vainycos
 * @description socket配置类
 * @date: 2023/7/10 9:44
 */
@Slf4j
@Configuration
public class SocketIOConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        // 解决重启端口占用问题，但因为是docker部署好像没有发现这个问题
        // socketConfig.setReuseAddress(true);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        // 设置主机名，默认是0.0.0.0
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        // 协议升级超时时间（毫秒）, 默认10000, HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息超时时间（毫秒）, 默认60000, 这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(pingTimeout);
        // Ping消息间隔（毫秒）, 默认25000, 客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(pingInterval);
        config.setTransports(Transport.POLLING);
        // 默认值为false
        config.setRandomSession(true);
        //鉴权
        config.setAuthorizationListener(data -> {
            // socketio可以传参，可以获取到 链接后面的参数 ?username=1&pwd=2
            String username= data.getSingleUrlParam("username");
            String pwd= data.getSingleUrlParam("pwd");
            log.info("获取到username.{}, pwd.{}", username, pwd);
            //这里可以用作链接时的权限判断，返回false就是不允许链接，
            return true;
        });

        // 异常
        config.setExceptionListener(new ExceptionListener() {
            @Override
            public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
                e.printStackTrace();
            }

            @Override
            public void onDisconnectException(Exception e, SocketIOClient client) {
                e.printStackTrace();
            }

            @Override
            public void onConnectException(Exception e, SocketIOClient client) {
                e.printStackTrace();
            }

            @Override
            public void onPingException(Exception e, SocketIOClient client) {
                e.printStackTrace();
            }

            @Override
            public void onPongException(Exception e, SocketIOClient socketIOClient) {
                e.printStackTrace();
            }

            @Override
            public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                cause.printStackTrace();
                ctx.close();
                return false;
            }
        });
        return new SocketIOServer(config);
    }

    /**
     * 开启SocketIOServer注解支持
     * @param socketServer
     * @return
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}