package dev.rico.internal.client.projector.factories.converters;

import javafx.util.StringConverter;

public class PlainStringConverter extends StringConverter<Object> {

    @Override
    public String toString(final Object object) {
        if (object == null) return null;
        return object.toString();
    }

    @Override
    public Object fromString(final String string) {
        return string;
    }
}
