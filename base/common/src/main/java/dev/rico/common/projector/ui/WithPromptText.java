package dev.rico.common.projector.ui;

import dev.rico.remoting.Property;

public interface WithPromptText {
    String getPromptText();

    void setPromptText(String promptText);

    Property<String> promptTextProperty();
}
