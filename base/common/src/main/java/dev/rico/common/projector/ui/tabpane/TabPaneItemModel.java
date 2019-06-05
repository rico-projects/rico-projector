package dev.rico.common.projector.ui.tabpane;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class TabPaneItemModel extends ItemModel {
    private Property<ItemModel> content;
    private Property<String> caption;

    public ItemModel getContent() {
        return content.get();
    }

    public void setContent(ItemModel content) {
        this.content.set(content);
    }

    public Property<ItemModel> contentProperty() {
        return content;
    }

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
