package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;

public class ProgressIndicatorModel extends ItemModel {
    private Property<Boolean> waiting;

    public Boolean getWaiting() {
        return waiting.get();
    }

    public void setWaiting(Boolean waiting) {
        this.waiting.set(waiting);
    }

    public Property<Boolean> waitingProperty() {
        return waiting;
    }
}
