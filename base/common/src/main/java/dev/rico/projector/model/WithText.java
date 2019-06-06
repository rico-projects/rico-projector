package dev.rico.projector.model;

import dev.rico.remoting.Property;

public interface WithText<M extends WithText> {

    Property<String> textProperty();

    default String getText() {
        return textProperty().get();
    }

    default void setText(final String text) {
        textProperty().set(text);
    }

    default M withText(final String text) {
        setText(text);
        return (M) this;
    }
}
