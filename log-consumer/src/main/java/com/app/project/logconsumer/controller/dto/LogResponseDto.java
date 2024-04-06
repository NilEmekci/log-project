package com.app.project.logconsumer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogResponseDto {


    private String time;
    private Map<String, String> countryData;


}
