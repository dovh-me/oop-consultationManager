package constants;

import java.time.format.DateTimeFormatter;

public class Formats {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static final DateTimeFormatter DATE_TIME_OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm a");

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
}
