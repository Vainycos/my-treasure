package com.vainycos;

import com.vainycos.service.ISocketIoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: Vainycos
 * @description socketio服务
 * @date: 2023/7/10 10:12
 */
@Component
public class SocketIoServer implements ApplicationRunner {

    @Autowired
    private ISocketIoService socketIOService;

    @Override
    public void run(ApplicationArguments args) {
        socketIOService.start();
    }

}
