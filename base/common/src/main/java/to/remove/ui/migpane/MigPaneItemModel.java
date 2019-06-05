package to.remove.ui.migpane;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class MigPaneItemModel extends IdentifiableModel {
    private Property<ItemModel> item;
    private Property<String> constraints;

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }

    public String getConstraints() {
        return constraints.get();
    }

    public void setConstraints(String constraints) {
        this.constraints.set(constraints);
    }

    public Property<String> constraintsProperty() {
        return constraints;
    }
}
