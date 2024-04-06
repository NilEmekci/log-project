package com.app.project.logconsumer.controller;

import com.app.project.logconsumer.utility.DateUtils;
import com.app.project.logconsumer.controller.dto.LogResponseDto;
import com.app.project.logconsumer.service.LogConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/log-consumer")
@RequiredArgsConstructor
@CrossOrigin
public class LogConsumerController {

    private  final LogConsumerService logConsumerService;

    @GetMapping("/getByDay/{timestamp}")
    public ResponseEntity<List<LogResponseDto>> getByDay(@PathVariable String timestamp){
        try {
            Date date = DateUtils.parseISO8601Date(timestamp);
            return ResponseEntity.ok(logConsumerService.getByDay(date));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
