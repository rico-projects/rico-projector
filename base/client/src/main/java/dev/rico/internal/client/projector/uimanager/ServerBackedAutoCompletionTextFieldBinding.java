package dev.rico.internal.client.projector.uimanager;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.util.Collection;

public class ServerBackedAutoCompletionTextFieldBinding<T> extends AutoCompletionBinding<T> {
    private final ChangeListener<String> textChangeListener;
    private final ChangeListener<Boolean> focusChangedListener;
    private final PauseTransition pause = new PauseTransition(Duration.millis(300));
    private StringConverter<T> converter;

    ServerBackedAutoCompletionTextFieldBinding(TextField textField, Callback<ISuggestionRequest, Collection<T>> suggestionProvider, StringConverter<T> converter) {
        super(textField, suggestionProvider, converter);
        this.textChangeListener = (obs, oldText, newText) -> {
            if (ServerBackedAutoCompletionTextFieldBinding.this.getCompletionTarget().isFocused()) {
                pause.setOnFinished(event -> ServerBackedAutoCompletionTextFieldBinding.this.setUserInput(newText));
                pause.playFromStart();
            }

        };
        this.focusChangedListener = (obs, oldFocused, newFocused) -> {
            if (!newFocused) {
                ServerBackedAutoCompletionTextFieldBinding.this.hidePopup();
            }
        };
        this.converter = converter;
        this.getCompletionTarget().textProperty().addListener(this.textChangeListener);
        this.getCompletionTarget().focusedProperty().addListener(this.focusChangedListener);
    }

    public TextField getCompletionTarget() {
        return (TextField) super.getCompletionTarget();
    }

    public void dispose() {
        this.getCompletionTarget().textProperty().removeListener(this.textChangeListener);
        this.getCompletionTarget().focusedProperty().removeListener(this.focusChangedListener);
    }

    protected void completeUserInput(T completion) {
        String newText = this.converter.toString(completion);
        this.getCompletionTarget().setText(newText);
        this.getCompletionTarget().positionCaret(newText.length());
        pause.stop();
    }
}
