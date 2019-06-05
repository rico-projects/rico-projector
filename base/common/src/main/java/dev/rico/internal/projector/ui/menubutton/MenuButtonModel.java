package dev.rico.internal.projector.ui.menubutton;

import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class MenuButtonModel extends ItemListContainerModel {
    private ObservableList<MenuButtonItemModel> items;
    private Property<String> caption;

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public ObservableList<MenuButtonItemModel> getItems() {
        return items;
    }
}
