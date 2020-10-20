package com.zdl.rabbit.configration;

import com.google.common.collect.Maps;
import com.zdl.rabbit.constant.ExchangeName;
import com.zdl.rabbit.constant.QueueName;
import com.zdl.rabbit.constant.RoutingKey;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、TTL ，即 Time-To-Live，存活时间，消息和队列都可以设置存活时间
 * 2、Dead Letter，即死信，若给消息设置了存活时间，当超过存活时间后消息还没有被消费，则该消息变成了死信
 * 3、Dead Letter Exchanges（DLX），即死信交换机
 * 4、Dead Letter Routing Key （DLK），死信路由键
 *
 * @author zhoudeli
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    /**
     * 创建一个立即消费队列
     * @return Queue
     */
    @Bean(QueueName.ImmediateQueue)
    public Queue immediateQueue() {
        return new Queue(QueueName.ImmediateQueue);
    }

    @Bean(ExchangeName.IMMEDIATE)
    public DirectExchange immediateExchange() {
        return new DirectExchange(ExchangeName.IMMEDIATE);
    }

    /**
     * 把 立即消费的队列 和 立即消费的exchange 绑定在一起
     * @param queue 立即消费队列
     * @param exchange 立即消费交换机
     * @return Binding
     */
    @Bean
    public Binding immediateBinding(@Qualifier(QueueName.ImmediateQueue) Queue queue, @Qualifier(ExchangeName.IMMEDIATE) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RoutingKey.IMMEDIATE_ROUTING_KEY);
    }


    /**
     * 创建一个延时队列
     * @return Queue
     */
    @Bean(QueueName.DelayQueue)
    public Queue delayQueue() {
        Map<String, Object> params = Maps.newHashMap();

        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        params.put("x-dead-letter-exchange", ExchangeName.IMMEDIATE);

        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key
        params.put("x-dead-letter-routing-key", RoutingKey.IMMEDIATE_ROUTING_KEY);

        // 设置队列中消息的过期时间，单位 毫秒
        params.put("x-message-ttl", 5 * 1000);

        return new Queue(QueueName.DelayQueue, true, false, false, params);
    }

    @Bean(ExchangeName.DELAY)
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(ExchangeName.DELAY);
    }

    /**
     * 把 延迟消费的队列 和 延迟消费的exchange 绑定在一起
     * @param queue 延时队列
     * @param exchange 延时交换机
     * @return
     */
    @Bean
    public Binding delayBinding(@Qualifier(QueueName.DelayQueue) Queue queue, @Qualifier(ExchangeName.DELAY) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RoutingKey.DELAY_KEY);
    }
}