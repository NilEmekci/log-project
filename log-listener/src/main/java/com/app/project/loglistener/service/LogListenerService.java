package com.app.project.loglistener.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LogListenerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Set<String> sentLogs; // Track sent logs

    @Autowired
    public LogListenerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.sentLogs = new HashSet<>();
    }

    @PostConstruct
    public void startLogListener() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::readLogFileAndSendToKafka, 0, 1, TimeUnit.SECONDS);
    }

    private void readLogFileAndSendToKafka() {
        String logFilePath = "C:\\Users\\Asus\\IdeaProjects\\log-project\\logs\\log.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!sentLogs.contains(line)) { // Check if the log has been sent before
                    kafkaTemplate.send(new ProducerRecord<>("log-topic", line));
                    log.info("Log sent to Kafka: {} " , line);
                    sentLogs.add(line); // Add the log to the set of sent logs
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
