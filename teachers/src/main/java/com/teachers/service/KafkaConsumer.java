package com.teachers.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my_topic", groupId = "test-group")
    public void listen(String message) {
        System.out.println("Received data from student: " + message);
    }
}

