package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class LatLongModel extends ItemModel {
    private Property<Double> latitude;
    private Property<Double> longitude;

    public Double getLatitude() {
        return latitude.get();
    }

    public void setLatitude(Double latitude) {
        this.latitude.set(latitude);
    }

    public Property<Double> latitudeProperty() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude.get();
    }

    public void setLongitude(Double longitude) {
        this.longitude.set(longitude);
    }

    public Property<Double> longitudeProperty() {
        return longitude;
    }
}
