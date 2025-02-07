package com.teachers.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue studentCreatedQueue() {
        return new Queue("studentCreatedQueue", false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("studentExchange");
    }

    @Bean
    public Binding binding(Queue studentCreatedQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(studentCreatedQueue)
                .to(directExchange)
                .with("student.created");
    }
}

