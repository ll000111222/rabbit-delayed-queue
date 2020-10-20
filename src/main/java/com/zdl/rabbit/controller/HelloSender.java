package com.zdl.rabbit.controller;

import com.zdl.rabbit.constant.ExchangeName;
import com.zdl.rabbit.constant.RoutingKey;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoudeli
 */
@RestController
@Slf4j
public class HelloSender {
    @Autowired
    private RabbitTemplate template;

    @GetMapping("/test")
    public String test() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("15800000000");

        log.info("开始发送消息");
        template.convertAndSend(ExchangeName.DELAY, RoutingKey.DELAY_KEY, userInfo);
        return "成功";
    }
}
