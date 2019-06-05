package dev.rico.internal.projector.ui.tabpane;


import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Side;

@RemotingBean
public class TabPaneModel extends ItemListContainerModel<TabPaneItemModel> {
    private ObservableList<TabPaneItemModel> items;
    private Property<Side> sideProperty;

    @Override
    public ObservableList<TabPaneItemModel> getItems() {
        return items;
    }

    public Property<Side> sideProperty() {
        return sideProperty;
    }

    public void setSide(Side side) {
        sideProperty.set(side);
    }

    public Side getSide() {
        return sideProperty.get();
    }
}
