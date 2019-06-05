package dev.rico.common.projector.ui.splitpane;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Orientation;

@RemotingBean
public class SplitPaneModel extends ItemModel {
    private Property<Orientation> orientation;
    private ObservableList<SplitPaneItemModel> items;

    public Orientation getOrientation() {
        return orientation.get();
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Property<Orientation> orientationProperty() {
        return orientation;
    }

    public ObservableList<SplitPaneItemModel> getItems() {
        return items;
    }
}
