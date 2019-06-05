package dev.rico.common.projector.ui;

import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ScrollPaneModel extends ItemModel {
    private Property<ItemModel> content;

    public Property<ItemModel> contentProperty() {
        return content;
    }

    public ItemModel getContent() {
        return content.get();
    }

    public void setContent(ItemModel content) {
        this.content.set(content);
    }
}
