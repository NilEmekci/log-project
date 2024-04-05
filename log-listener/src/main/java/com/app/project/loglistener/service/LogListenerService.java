package com.app.project.loglistener.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogListenerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    public void logListener() {
        String logFilePath = "C:\\Users\\Asus\\IdeaProjects\\log-project\\logs\\log.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                kafkaTemplate.send(new ProducerRecord<>("log-topic", line));
                System.out.println("Log sent to Kafka: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
