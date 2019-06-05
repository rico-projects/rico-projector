package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.propertysheet.PropertySheetModel;
import dev.rico.client.remoting.ControllerProxy;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class PropertySheet extends Control {
    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;
    private final PropertySheetModel model;

    public PropertySheet(ControllerProxy<? extends ManagedUiModel> controllerProxy, PropertySheetModel model) {
        this.controllerProxy = controllerProxy;
        this.model = model;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        if (model.getSkin() == null || model.getSkin() == PropertySheetModel.Skin.MasterDetail) {
            return new MasterDetailPropertySheetSkin(this, controllerProxy, model);
        } else {
            return new PropertySheetSkin(this, controllerProxy, model);
        }
    }
}
