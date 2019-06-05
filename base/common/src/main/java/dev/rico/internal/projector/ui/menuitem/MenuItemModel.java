package dev.rico.internal.projector.ui.menuitem;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class MenuItemModel extends IdentifiableModel {
    private ObservableList<MenuItemModel> items;
    private Property<String> caption;
    private Property<ItemModel> graphic;
    private Property<String> action;

    public ObservableList<MenuItemModel> getItems() {
        return items;
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

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

    public ItemModel getGraphic() {
        return graphic.get();
    }

    public void setGraphic(ItemModel graphic) {
        this.graphic.set(graphic);
    }

    public Property<ItemModel> graphicProperty() {
        return graphic;
    }
}
