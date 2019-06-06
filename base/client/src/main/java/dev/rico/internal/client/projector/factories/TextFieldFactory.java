package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import java.util.Optional;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.projector.ui.TextFieldModel;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

public class TextFieldFactory implements ProjectorNodeFactory<TextFieldModel, TextField> {

    @Override
    public TextField create(final Projector projector, final TextFieldModel model) {
        TextField textField = new TextField();
        bind(textField.prefColumnCountProperty()).to(model.prefColumnCountProperty());
        bind(textField.promptTextProperty()).bidirectionalTo(model.promptTextProperty());
        bind(textField.editableProperty()).to(model.editableProperty(), value -> getValue(value, true));
        bind(textField.textProperty()).bidirectionalTo(model.textProperty());
        configureTextInputControl(projector.getControllerProxy(), model, textField);
        return textField;
    }

    @Override
    public Class<TextFieldModel> getSupportedType() {
        return  TextFieldModel.class;
    }


    private void configureTextInputControl(ControllerProxy<?> controllerProxy, TextFieldModel model, TextInputControl control) {
        if (model.getAction() != null) {
            final PauseTransition pause = getPauseTransition(model);
            control.textProperty().addListener((observable, oldValue, newValue) -> {
                pause.setOnFinished(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog
                        .showError(control, throwable)));
                pause.playFromStart();
            });
        }
    }

    private PauseTransition getPauseTransition(TextFieldModel model) {
        return new PauseTransition(
                Duration.millis(Optional.ofNullable(model.getActionDelay()).orElse(850)));
    }
}
