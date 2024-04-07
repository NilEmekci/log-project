package com.app.project.loglistener;

import com.app.project.loglistener.service.LogListenerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.mockito.Mockito.*;

@SpringBootTest
class LogListenerServiceTest {

    private LogListenerService service;

    private KafkaTemplate<String, String> kafkaTemplate;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        kafkaTemplate = mock(KafkaTemplate.class);

        service = new LogListenerService(kafkaTemplate);

        setField(service, "logFilePath", tempDir.resolve("log.txt").toString());
        setField(service, "lastReadPositionFilePath", tempDir.resolve("log-remember.txt").toString());
    }

    @Test
    void readLogFileAndSendToKafkaWithContent() throws IOException {
        Files.write(tempDir.resolve("log.txt"), "Test log entry\nAnother log entry".getBytes());
        Files.write(tempDir.resolve("log-remember.txt"), "0".getBytes());

        service.readLogFileAndSendToKafka();

        verify(kafkaTemplate, times(2)).send(any(ProducerRecord.class));
    }

    @Test
    void readLogFileAndSendToKafkaWithEmptyFile() throws IOException {
        Files.createFile(tempDir.resolve("log.txt"));
        Files.write(tempDir.resolve("log-remember.txt"), "0".getBytes());

        service.readLogFileAndSendToKafka();

        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }


    private void setField(Object targetObject, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, value);
    }
}
