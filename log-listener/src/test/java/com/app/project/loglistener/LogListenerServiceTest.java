package com.app.project.loglistener;

import com.app.project.loglistener.service.LogListenerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.HashSet;
import static org.mockito.Mockito.*;

@SpringBootTest
class LogListenerServiceTest {

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @InjectMocks
    private LogListenerService logListenerService;

    @TempDir
    Path tempDir;

    @Test
    void testReadLogFileAndSendToKafka() throws Exception {

        String logData = "2024-04-07 16:51:29.593 FATAL Istanbul tkycljykclanmz";
        File tempLogFile = tempDir.resolve("log.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempLogFile))) {
            writer.write(logData);
        }

        logListenerService.logFilePath = tempLogFile.getAbsolutePath();

        logListenerService.sentLogs = new HashSet<>();

        logListenerService.readLogFileAndSendToKafka();

        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
    }
}
