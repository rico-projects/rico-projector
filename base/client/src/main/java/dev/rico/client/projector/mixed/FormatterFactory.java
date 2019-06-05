package dev.rico.client.projector.mixed;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterFactory {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern()).withLocale(Locale.ENGLISH).withZone(ZoneId.of("UTC"));

    public static String datePattern() {
        return "d. MMM yyyy";
    }

    public static DateTimeFormatter dateFormatter() {
        return dateFormatter;
    }

    public static DateTimeFormatter customFormat(String formatString) {
        return DateTimeFormatter.ofPattern(formatString).withLocale(Locale.ENGLISH).withZone(ZoneId.of("UTC"));
    }
}
