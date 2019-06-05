package dev.rico.common.projector.ui.splitpane;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class SplitPaneItemModel extends ItemModel {
    private Property<ItemModel> content;
    private Property<Double> dividerPosition;

    public ItemModel getContent() {
        return content.get();
    }

    public void setContent(ItemModel content) {
        this.content.set(content);
    }

    public Property<ItemModel> contentProperty() {
        return content;
    }

    public Double getDividerPosition() {
        return dividerPosition.get();
    }

    public void setDividerPosition(Double dividerPosition) {
        this.dividerPosition.set(dividerPosition);
    }

    public Property<Double> dividerPositionProperty() {
        return dividerPosition;
    }
}
