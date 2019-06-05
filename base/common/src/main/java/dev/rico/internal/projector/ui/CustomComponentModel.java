package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class CustomComponentModel extends ItemModel {
    private Property<String> type;

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
