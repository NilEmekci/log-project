package com.app.project.loglistener.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogListenerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public Set<String> sentLogs;
    @Value("${log.file.path}")
    public String logFilePath;



    @PostConstruct
    public void startLogListener() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::readLogFileAndSendToKafka, 0, 1, TimeUnit.SECONDS);
    }

    public void readLogFileAndSendToKafka() {
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!sentLogs.contains(line)) {
                    kafkaTemplate.send(new ProducerRecord<>("log-topic", line));
                    log.info("Log sent to Kafka: {} " , line);
                    sentLogs.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


