package dev.rico.client.projector.uimanager;

import dev.rico.common.projector.ui.ManagedUiModel;
import dev.rico.common.projector.ui.autocompletion.AutoCompleteItemModel;
import dev.rico.common.projector.ui.autocompletion.AutoCompleteModel;
import com.sun.javafx.scene.traversal.Direction;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.Param;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static dev.rico.client.remoting.FXBinder.bind;


public class ServerBackedAutoCompletionField extends CustomTextField {
    ServerBackedAutoCompletionField(ControllerProxy<? extends ManagedUiModel> controllerProxy, AutoCompleteModel model) {
        bind(prefColumnCountProperty()).to(model.prefColumnCountProperty());
        bind(promptTextProperty()).to(model.promptTextProperty());
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.prefHeightProperty().bind(heightProperty());
        progressIndicator.prefWidthProperty().bind(heightProperty());
        progressIndicator.setVisible(false);
        setRight(progressIndicator);

        StringConverter<AutoCompleteItemModel> stringConverter = new StringConverter<AutoCompleteItemModel>() {
            @Override
            public String toString(AutoCompleteItemModel object) {
                if (object == null) {
                    return "";
                }
                return object.getCaption();
            }

            @Override
            public AutoCompleteItemModel fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return model.getItems().stream().filter(item -> item.getCaption().equals(string)).findFirst().orElse(null);
            }
        };
        TextFormatter<AutoCompleteItemModel> textFormatter = new TextFormatter<>(stringConverter);
        setTextFormatter(textFormatter);

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<AutoCompleteItemModel>> suggestionProvider = suggestionRequest -> {
            Platform.runLater(() -> progressIndicator.setVisible(true));
            try {
//                System.out.println("Calling action 'findMatches' for '" + suggestionRequest.getUserText() + "'");
                if (model.getSearchAction() != null)
                    controllerProxy.invoke(model.getSearchAction(), new Param("autoCompleteField", model), new Param("searchPhrase", suggestionRequest.getUserText())).get();
                else
                    controllerProxy.invoke("findMatches", new Param("autoCompleteField", model), new Param("searchPhrase", suggestionRequest.getUserText())).get();
            } catch (InterruptedException e) {
                return new ArrayList<>();
            } catch (ExecutionException e) {
                UnexpectedErrorDialog.showError(this, e);
            } finally {
                Platform.runLater(() -> progressIndicator.setVisible(false));
            }
            return new ArrayList<>(model.getItems());
        };

        ServerBackedAutoCompletionTextFieldBinding<AutoCompleteItemModel> binding = new ServerBackedAutoCompletionTextFieldBinding<>(this, suggestionProvider, stringConverter);
        binding.setDelay(0);
        binding.setOnAutoCompleted(event -> {
            if (model.getAction() != null)
                controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(this, throwable));
            impl_traverse(Direction.NEXT);
        });

        focusedProperty().addListener(observable -> {
            if (textFormatter.getValue() == null) {
                clear();
            }
        });

        textFormatter.valueProperty().addListener((observable, oldValue, newValue) -> {
            model.getItems().clear();
            if (newValue != null) {
                model.getItems().add(newValue);
            }
        });
        textFormatter.setValue(model.getSelected());

        bind(model.selectedProperty()).bidirectionalTo(textFormatter.valueProperty());

        addEventHandler(KeyEvent.KEY_PRESSED, (evt) -> {
            if (evt.getCode() == KeyCode.SPACE && evt.isControlDown()) {
                binding.setUserInput(null);
            }
        });
        AutoCompleteField.enableAutoSelectAll(this);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AutoCompletionFieldSkin(this) {
            @Override
            public ObjectProperty<Node> rightProperty() {
                return ServerBackedAutoCompletionField.this.rightProperty();
            }
        };
    }
}
