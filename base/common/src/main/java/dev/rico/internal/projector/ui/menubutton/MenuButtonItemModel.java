package dev.rico.internal.projector.ui.menubutton;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class MenuButtonItemModel extends IdentifiableModel {
    private Property<String> caption;

    private Property<String> action;

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }
}
