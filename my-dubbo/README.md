> 集成dubbo rpc调用。

# 目录结构
1. [api](./api):接口定义
2. [consumer](./consumer):服务使用者
3. [provider](./provider):服务提供者

# 版本依赖
1. 使用zookeeper,本地2181端口
```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-dependencies-zookeeper</artifactId>
    <version>2.7.6</version>
</dependency>
```

2. 使用dubbo-starter
```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.6</version>
</dependency>
```
# 注意点
## api定义的接口由provider模块实现，详见[OrderServiceImpl](./provider/src/main/java/com/vainycos/provider/service/OrderServiceImpl.java)
使用@Service注解，不是spring的类库而是dubbo类库下的@Service
```java
import org.apache.dubbo.config.annotation.Service;

@Service
public class OrderServiceImpl implements OrderService {
    
    
}
```

## consumer模块使用api模块的接口并最终使用到provider的实现方法，详见[UserServiceImpl](./consumer/src/main/java/com/vainycos/consumer/service/impl/UserServiceImpl.java)
1. consumer模块中，使用dubbo的@Reference注解，注入接口实现类
2. 但是标记服务类的@Service与上面provider需要使用dubbo下的@Service不同，此处需要使用spring下的@Service注解


```java
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Reference
    private OrderService orderService;
}
```

# 启动
1. 由于我们将dubbo服务与zookeeper结合在一起，所以目前需要依赖zookeeper服务。我们启动一个2181端口的zookeeper服务，将provider服务注册上去。
推荐使用docker进行zookeeper的部署，若是桌面客户端则可以使用Docker Desktop软件。
2. 依次启动provider，consumer服务。
3. 校验，访问http://localhost:50001/getUser?userId=1,得到以下结果并能够在provider控制台上看到输出获取订单消息...则说明成功
```json
[
  {
    "id": 1,
    "createTime": null,
    "userId": 1,
    "orderItemList": null
  }
]
```
# 备注
我们可以到zookeeper中使用相关命令看到我们注册的服务节点，这里以docker部署为例：
1. 获取容器的id
```shell
docker ps
```
2. 进入到容器内部:
```shell
docker exec -it ${CONTAINER ID} /bin/bash
```
3. 进入bin目录，并执行./zkCli.sh脚本，连接zookeeper客户端
```shell
cd /bin
./zkCli.sh
```

4. 查看注册节点
```shell
# 此处可以看到我们注册的provider模块的dubbo节点以及默认的zookeeper节点
# 输出：[dubbo, zookeeper]
ls /
```

5. 查看节点详情
```shell
# ls /节点名称
# 输出：[configurators, consumers, providers, routers]
ls /dubbo/com.vainycos.api.api_interface.OrderService
```
实际上我们还可以继续查看下层节点的详情继续下钻，类似于树节点的结构或者目录的结构。

参考资料：
- [SpringBoot集成Dubbo](https://blog.csdn.net/MX__LL/article/details/125537275)