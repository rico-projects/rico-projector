package dev.rico.client.projector.uimanager;

import com.sun.javafx.scene.traversal.Direction;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AutoCompleteField<T> extends TextField {

    private final TextFormatter<T> formatter = new TextFormatter<>(new StringConverter<T>() {
        @Override
        public String toString(T object) {
            if (object == null) {
                return "";
            }
            return object.toString();
        }

        @Override
        public T fromString(String string) {
            if (string == null) return null;
            try {
                return convertFromString(string);
            } catch (Exception e) {
                // Exception werfen würde den zuletzt
                // gültigen Wert wieder herstellen
                return null;
            }
        }
    });

    AutoCompleteField() {
        setTextFormatter(formatter);

        AutoCompletionTextFieldBinding<String> binding = new AutoCompletionTextFieldBinding<>(this, this::findSuggestions);
        binding.setDelay(0);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> impl_traverse(Direction.NEXT));

        addEventHandler(KeyEvent.KEY_PRESSED, (evt) -> {
            if (evt.getCode() == KeyCode.SPACE && evt.isControlDown()) {
                binding.setUserInput(null);
            }
        });

        enableAutoSelectAll(this);
    }

    static void enableAutoSelectAll(final TextInputControl control) {
        control.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        Platform.runLater(() -> control.selectRange(control.getLength(), 0));
                    }
                });
    }

    TextFormatter<T> getFormatter() {
        return formatter;
    }

    private Collection<String> findSuggestions(ISuggestionRequest suggestionRequest) {
        ArrayList<String> result = new ArrayList<>();
        String userText = suggestionRequest.getUserText();
        if (userText != null) {
            result.addAll(getMatchingSuggestions(suggestionRequest));
        } else {
            result.addAll(getDefaultSuggestions(suggestionRequest));
        }
        return result;
    }

    protected abstract Collection<String> getDefaultSuggestions(ISuggestionRequest suggestionRequest);

    protected abstract Collection<String> getMatchingSuggestions(ISuggestionRequest suggestionRequest);

    abstract protected T convertFromString(String string);
}
