package dev.rico.internal.projector.ui;

import dev.rico.remoting.Property;

public interface WithCaption {
    String getCaption();

    void setCaption(String caption);

    Property<String> captionProperty();
}
