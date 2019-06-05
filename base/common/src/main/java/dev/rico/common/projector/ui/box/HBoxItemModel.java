package dev.rico.common.projector.ui.box;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.scene.layout.Priority;

@RemotingBean
public class HBoxItemModel extends IdentifiableModel {
    private Property<ItemModel> item;
    private Property<Priority> hGrow;

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }

    public Priority gethGrow() {
        return hGrow.get();
    }

    public void sethGrow(Priority hGrow) {
        this.hGrow.set(hGrow);
    }

    public Property<Priority> hGrowProperty() {
        return hGrow;
    }
}
