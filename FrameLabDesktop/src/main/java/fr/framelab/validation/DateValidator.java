package fr.framelab.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;

public class DateValidator {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                    .withResolverStyle(ResolverStyle.STRICT);

    public static void validate(String dateStr) {
        if (dateStr == null) {
            throw new IllegalArgumentException("Null date is not allowed");
        }

        if (dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty date is not allowed");
        }

        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid format. Expected format: yyyy-MM-dd HH:mm:ss");
        }

        try {
            LocalDateTime.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date: " + dateStr, e);
        }
    }
}