package dev.rico.client.projector.uimanager;

import dev.rico.common.projector.ui.TextFieldModel;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.core.functional.Binding;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

import java.util.Optional;

import static dev.rico.client.remoting.FXBinder.bind;

class TextField extends javafx.scene.control.TextField {
    public TextField(ControllerProxy<?> controllerProxy, TextFieldModel model) {
        bind(prefColumnCountProperty()).to(model.prefColumnCountProperty());
        configureTextInputControl(controllerProxy, model, this);
    }

    static Binding configureTextInputControl(ControllerProxy<?> controllerProxy, TextFieldModel model, TextInputControl control) {
        bind(control.promptTextProperty()).bidirectionalTo(model.promptTextProperty());
        bind(control.editableProperty()).to(model.editableProperty(), value -> value == null ? true : value);
        if (model.getAction() != null) {
            final PauseTransition pause = new PauseTransition(Duration.millis(Optional.ofNullable(model.getActionDelay()).orElse(850)));
            control.textProperty().addListener((observable, oldValue, newValue) -> {
                pause.setOnFinished(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(control, throwable)));
                pause.playFromStart();
            });
        }
        return bind(control.textProperty()).bidirectionalTo(model.textProperty());
    }
}
