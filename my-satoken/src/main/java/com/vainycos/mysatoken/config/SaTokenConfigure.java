package com.vainycos.mysatoken.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author: Vainycos
 * @description sa-token配置类
 * @date: 2022/12/19 13:31
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(Handler -> {
            // 等同于下方的简写写法
            // SaRouter
            //        // 拦截的path列表 支持写多个
            //        .match("/**")
            //        // 排除掉的path 支持写多个
            //        .notMatch("/login")
            //        // 要执行的校验动作
            //        .check(r -> StpUtil.checkLogin());
            // 可以简写登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            SaRouter.match("/**", "/login", r -> StpUtil.checkLogin());
            // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            //SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));

            // 根据路由划分模块，不同模块不同鉴权
            //SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
        }));
    }
}
