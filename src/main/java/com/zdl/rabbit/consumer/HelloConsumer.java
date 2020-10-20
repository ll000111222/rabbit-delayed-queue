package com.zdl.rabbit.consumer;

import com.zdl.rabbit.constant.QueueName;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhoudeli
 */
@Component
@Slf4j
public class HelloConsumer {
    @RabbitListener(queues = QueueName.ImmediateQueue)
    public void process(UserInfo userInfo){
        log.info("------------userInfo: {}",userInfo);
    }
}
