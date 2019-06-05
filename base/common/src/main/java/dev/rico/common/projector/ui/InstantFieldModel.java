package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.time.Instant;

@RemotingBean
public class InstantFieldModel extends ItemModel {
    private Property<String> promptText;
    private Property<Instant> instant;

    public String getPromptText() {
        return promptText.get();
    }

    public void setPromptText(String promptText) {
        this.promptText.set(promptText);
    }

    public Property<String> promptTextProperty() {
        return promptText;
    }

    public Instant getInstant() {
        return instant.get();
    }

    public void setInstant(Instant instant) {
        this.instant.set(instant);
    }

    public Property<Instant> instantProperty() {
        return instant;
    }
}