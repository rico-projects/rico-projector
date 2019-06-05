package dev.rico.common.projector.ui.choicebox;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;

public class ChoiceBoxItemModel extends IdentifiableModel {
    private Property<String> caption;

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }
}
