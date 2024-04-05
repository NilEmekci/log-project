package com.app.project.logconsumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class LogConsumerService {
    
    @KafkaListener(topics = "log-topic", groupId = "log-consumer-group")
    public void consumeLog(ConsumerRecord<String, String> record) {
        String log = record.value();
        System.out.println("Received log: " + log);
    }


}
