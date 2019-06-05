package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.client.remoting.ControllerProxy;

import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;
import static dev.rico.client.remoting.FXBinder.bind;

public class TextArea extends javafx.scene.control.TextArea {
    public TextArea(ControllerProxy<?> controllerProxy, TextAreaModel model) {
        bind(prefColumnCountProperty()).to(model.prefColumnCountProperty());
        bind(prefRowCountProperty()).to(model.prefRowCountProperty());
        bind(wrapTextProperty()).to(model.wrapTextProperty());
        configureTextInputControl(controllerProxy, model, this);
    }
}
