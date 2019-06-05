package dev.rico.internal.projector.ui.box;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.scene.layout.Priority;

@RemotingBean
public class VBoxItemModel extends IdentifiableModel {
    private Property<ItemModel> item;
    private Property<Priority> vGrow;

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }

    public Priority getvGrow() {
        return vGrow.get();
    }

    public void setvGrow(Priority vGrow) {
        this.vGrow.set(vGrow);
    }

    public Property<Priority> vGrowProperty() {
        return vGrow;
    }
}
