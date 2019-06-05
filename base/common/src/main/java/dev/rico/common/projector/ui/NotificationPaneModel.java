package dev.rico.common.projector.ui;

import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class NotificationPaneModel extends ItemModel {
    private Property<String> text;
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

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Property<String> textProperty() {
        return text;
    }
}
