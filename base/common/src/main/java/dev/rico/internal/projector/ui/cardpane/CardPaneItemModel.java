package dev.rico.internal.projector.ui.cardpane;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class CardPaneItemModel extends IdentifiableModel {
    private Property<ItemModel> item;

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }
}