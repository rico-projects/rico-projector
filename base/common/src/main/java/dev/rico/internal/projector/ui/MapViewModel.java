package dev.rico.internal.projector.ui;


import dev.rico.internal.projector.ForRemoval;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
@ForRemoval
public class MapViewModel extends ItemModel {
    private Property<LatLongModel> position;

    public LatLongModel getPosition() {
        return position.get();
    }

    public void setPosition(LatLongModel position) {
        this.position.set(position);
    }

    public Property<LatLongModel> positionProperty() {
        return position;
    }
}
