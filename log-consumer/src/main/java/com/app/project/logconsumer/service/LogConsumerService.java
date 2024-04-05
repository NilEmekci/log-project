package com.app.project.logconsumer.service;

import com.app.project.logconsumer.LogEntity;
import com.app.project.logconsumer.LogResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class LogConsumerService {


    private final MongoTemplate mongoTemplate;

    @KafkaListener(topics = "log-topic", groupId = "log-consumer-group")
    public void consumeLog(ConsumerRecord<String, String> record) {
        String receivedLog = record.value();
        log.info("Received log: {} " , receivedLog);

        LogEntity logEntity = parseLogEntity(receivedLog);

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
        mongoTemplate.save(logEntity);
    }

    public List<LogResponseDto> getByDay(Date timestamp) {
        LocalDate localDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59);

        Query query = new Query(Criteria.where("timestamp").gte(startOfDay).lte(endOfDay));

        List<LogEntity> logs = mongoTemplate.find(query, LogEntity.class);

        Map<String, Map<String, Long>> groupedLogs = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> new SimpleDateFormat("HH:mm").format(log.getTimestamp()),
                        Collectors.groupingBy(LogEntity::getLocation, Collectors.counting())
                ));

        return groupedLogs.entrySet().stream()
                .map(entry -> {
                    LogResponseDto dto = new LogResponseDto();
                    dto.setTime(entry.getKey());
                    dto.setCountryData(entry.getValue().entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())));
                    return dto;
                }).collect(Collectors.toList());
    }
}
