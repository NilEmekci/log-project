package com.app.project.logconsumer.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static Date parseISO8601Date(String iso8601Date) throws ParseException {
        return ISO_DATE_FORMAT.parse(iso8601Date);
    }
}

