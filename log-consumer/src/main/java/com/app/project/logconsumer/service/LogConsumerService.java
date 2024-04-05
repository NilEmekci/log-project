package com.app.project.logconsumer.service;

import com.app.project.logconsumer.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Service
@Slf4j
public class LogConsumerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @KafkaListener(topics = "log-topic", groupId = "log-consumer-group")
    public void consumeLog(ConsumerRecord<String, String> record) {
        String receivedLog = record.value();
        log.info("Received log: {} " , receivedLog);

        // Log satırını parçalayarak LogEntity nesnesi oluşturma
        LogEntity logEntity = parseLogEntity(receivedLog);

        // MongoDB'ye kaydetme işlemi
        saveLogToMongoDB(logEntity);
    }

    private LogEntity parseLogEntity(String log) {
        String[] parts = log.split(" ");
        LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String level = parts[2];
        String location = parts[3];
        StringBuilder message = new StringBuilder();
        for (int i = 4; i < parts.length; i++) {
            message.append(parts[i]).append(" ");
        }

        return new LogEntity(convertToDate(timestamp), level, location, message.toString().trim());
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void saveLogToMongoDB(LogEntity logEntity) {
        // MongoDB'ye kaydetme işlemi
        mongoTemplate.save(logEntity);
    }
}
