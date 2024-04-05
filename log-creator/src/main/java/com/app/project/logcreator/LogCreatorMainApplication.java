package com.app.project.logcreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LogCreatorMainApplication {

    private static final Logger logger = LoggerFactory.getLogger(LogCreatorMainApplication.class);
    private static final String[] CITIES = {"Istanbul", "Tokyo", "Moskow", "Beijing", "London"};
    private static final String[] LOG_LEVELS = {"INFO", "WARN", "FATAL", "DEBUG", "ERROR"};
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get("./logs/"));
        File logFile = new File("logs/log.txt");

        if (logFile.exists()) {
            logFile.delete();
        }

        try {
            logFile.createNewFile();
            logger.info("Log file created: {} ", logFile.getAbsolutePath());

            while (true) {
                writeLog(logFile);

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeLog(File logFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            String city = CITIES[RANDOM.nextInt(CITIES.length)];
            String logLevel = LOG_LEVELS[RANDOM.nextInt(LOG_LEVELS.length)];
            String logDetail = generateRandomLogDetail();

            String logLine = timestamp + " " + logLevel + " " + city + " " + logDetail;
            writer.write(logLine);
            writer.newLine();
            logger.info("Log written: {} ", logLine);



        }
    }

    private static String generateRandomLogDetail() {
        StringBuilder sb = new StringBuilder();
        int length = RANDOM.nextInt(20) + 10;
        for (int i = 0; i < length; i++) {
            char c = (char) (RANDOM.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }

}
