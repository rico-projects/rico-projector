package dev.rico.client.projector.uimanager;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.ArrayList;

public class ProgressIndicator extends Control {
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(this, "active", true);

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProgressIndicatorSkin(this, new BehaviorBase<>(this, new ArrayList<>()));
    }

    public final BooleanProperty activeProperty() {
        return activeProperty;
    }

    public final boolean isActive() {
        return activeProperty.get();
    }

    public final void setActive(boolean value) {
        activeProperty.set(value);
    }
}
