package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

/**
 * Ein Container, der exakt ein Kind verwaltet.
 */
@RemotingBean
public class SingleItemContainerModel extends ItemModel {
    private Property<ItemModel> content;

    public ItemModel getContent() {
        return content.get();
    }

    public void setContent(ItemModel content) {
        this.content.set(content);
    }

    public Property<ItemModel> contentProperty() {
        return content;
    }
}
