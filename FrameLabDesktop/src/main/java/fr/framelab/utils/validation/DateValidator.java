package fr.framelab.utils.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;

public class DateValidator {
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Si la date n'a pas d'heure, on ajoute " 00:00:00" */
    public static String normalize(String dateStr) {
        if (dateStr != null && dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return dateStr + " 00:00:00";
        }
        return dateStr;
    }

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