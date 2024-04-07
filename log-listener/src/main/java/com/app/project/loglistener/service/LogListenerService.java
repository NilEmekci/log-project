package com.app.project.loglistener.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogListenerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private long lastPosition = 0;
    @Value("${log.file.path}")
    private String logFilePath;
    @Value("${log.file.last-read-position.file.path}")
    private String lastReadPositionFilePath;

    @PostConstruct
    public void startLogListener() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        readLastPosition();
        scheduler.scheduleAtFixedRate(this::readLogFileAndSendToKafka, 0, 1, TimeUnit.SECONDS);
    }

    private void readLastPosition() {
        try {
            File lastReadFile = new File(lastReadPositionFilePath);
            if (lastReadFile.exists()) {
                log.info("Log position file found in {} path, reading last position", lastReadPositionFilePath);
                BufferedReader br = new BufferedReader(new FileReader(lastReadFile));
                lastPosition = Long.parseLong(br.readLine());
                br.close();
            }
        } catch (IOException | NumberFormatException e) {
            log.error("Error reading last read position: ", e);
        }
    }

    private void saveLastPosition() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(lastReadPositionFilePath));
            writer.write(Long.toString(lastPosition));
            writer.close();
        } catch (IOException e) {
            log.error("Error saving last read position: ", e);
        }
    }

    public void readLogFileAndSendToKafka() {
        try (RandomAccessFile accessFile = new RandomAccessFile(logFilePath, "r")) {
            if(accessFile.length() < lastPosition) {
                lastPosition = 0;
            }
            accessFile.seek(lastPosition);
            String line;
            while ((line = accessFile.readLine()) != null) {
                kafkaTemplate.send(new ProducerRecord<>("log-topic", line));
                log.info("Log sent to Kafka: {}", line);
                lastPosition = accessFile.getFilePointer();
            }
            saveLastPosition();
        } catch (FileNotFoundException e) {
            log.error("Log file not found: ", e);
        } catch (IOException e) {
            log.error("Error reading log file: ", e);
        }
    }
}


