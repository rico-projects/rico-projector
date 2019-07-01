package dev.rico.internal.client.projector.mixed;

import dev.rico.internal.core.RicoConstants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterFactory {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern()).withLocale(Locale.ENGLISH).withZone(ZoneId.of(RicoConstants.TIMEZONE_UTC));

    public static String datePattern() {
        return "d. MMM yyyy";
    }

    public static DateTimeFormatter dateFormatter() {
        return dateFormatter;
    }

    public static DateTimeFormatter customFormat(final String formatString) {
        return DateTimeFormatter.ofPattern(formatString).withLocale(Locale.ENGLISH).withZone(ZoneId.of(RicoConstants.TIMEZONE_UTC));
    }
}
