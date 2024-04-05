package com.app.project.logconsumer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "logs")
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

    // Getter ve setter metotlarını ekleyebilirsiniz

    @Override
    public String toString() {
        return "LogEntity{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", location='" + location + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}