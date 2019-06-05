package dev.rico.common.projector.ui.autocompletion;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class AutoCompleteItemModel extends IdentifiableModel {
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
