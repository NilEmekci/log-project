package com.app.project.logconsumer;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.app.project.logconsumer.controller.dto.LogResponseDto;
import com.app.project.logconsumer.model.LogEntity;
import com.app.project.logconsumer.service.LogConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LogConsumerServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private LogConsumerService logConsumerService;

    private ConsumerRecord<String, String> record;

    @BeforeEach
    void setUp() {
        String logString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " INFO testLocation testMessage";
        record = new ConsumerRecord<>("log-topic", 0, 0, "key", logString);
    }

    @Test
    void consumeLogTest() {
        logConsumerService.consumeLog(record);
        verify(mongoTemplate).save(any(LogEntity.class));
    }

    @Test
    void getByDayTest() {

        when(mongoTemplate.find(any(), any(Class.class)))
                .thenReturn(Arrays.asList(new LogEntity(new Date(), "INFO", "testLocation", "testMessage")));

        List<LogResponseDto> responseDtos = logConsumerService.getByDay(new Date());

        assertThat(responseDtos).isNotEmpty();
        assertThat(responseDtos).hasSize(1);

        LogResponseDto responseDto = responseDtos.get(0);
        assertThat(responseDto.getTime()).isNotNull();
        assertThat(responseDto.getCountryData()).isNotEmpty();
        assertThat(responseDto.getCountryData()).containsKeys("testLocation");
        assertThat(responseDto.getCountryData().get("testLocation")).isEqualTo("1");
    }
}
