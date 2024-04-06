package com.app.project.logconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogEntity {

    @Id
    private String id;
    private Date timestamp;
    private String level;
    private String location;
    private String message;

    public LogEntity(Date timestamp, String level, String location, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.location = location;
        this.message = message;
    }


}