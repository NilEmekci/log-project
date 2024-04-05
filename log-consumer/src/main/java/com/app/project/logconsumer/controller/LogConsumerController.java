package com.app.project.logconsumer.controller;

import com.app.project.logconsumer.DateUtils;
import com.app.project.logconsumer.LogResponseDto;
import com.app.project.logconsumer.service.LogConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/log-consumer")
@RequiredArgsConstructor
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