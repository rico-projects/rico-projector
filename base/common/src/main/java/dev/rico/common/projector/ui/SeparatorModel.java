package dev.rico.common.projector.ui;

import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Orientation;

@RemotingBean
public class SeparatorModel extends ItemModel {
    private Property<Orientation> orientation;

    public Orientation getOrientation() {
        return orientation.get();
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Property<Orientation> orientationProperty() {
        return orientation;
    }
}
