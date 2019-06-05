package dev.rico.common.projector.ui.cardpane;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.common.projector.ui.ItemModel;
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
