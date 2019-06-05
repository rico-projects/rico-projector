package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.client.remoting.ControllerProxy;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

import static dev.rico.client.remoting.FXWrapper.wrapList;

public class ManagedChoiceBox extends ChoiceBox<ChoiceBoxItemModel> {
    public ManagedChoiceBox(ChoiceBoxModel model, ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        setConverter(new StringConverter<ChoiceBoxItemModel>() {
            @Override
            public String toString(ChoiceBoxItemModel object) {
                return object.getCaption();
            }

            @Override
            public ChoiceBoxItemModel fromString(String string) {
                return null;
            }
        });
        setModel(model, controllerProxy);
    }

    public void setModel(ChoiceBoxModel model, ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        setItems(wrapList(model.getItems()));
        ClientUiHelper.bindWithSelectionModel(model.selectedProperty(), getSelectionModel());
        if (model.getAction() != null) {
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(this, throwable));
            });
        }
    }
}
