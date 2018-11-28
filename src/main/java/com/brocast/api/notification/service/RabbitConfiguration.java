package com.brocast.api.notification.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */

@EnableRabbit
@Configuration
public class RabbitConfiguration {
    Logger logger = Logger.getLogger(RabbitConfiguration.class);
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue liveQ() {
        return new Queue("liveLNQ");
    }
    @Bean
    public Queue mediaQ() {
        return new Queue("liveMNQ");
    }
    @Bean
    public Queue multiMainQ() {
        return new Queue("liveMMAILNQ");
    }
    @Bean
    public Queue unoMailQ() {
        return new Queue("liveUMAILNQ");
    }
}
