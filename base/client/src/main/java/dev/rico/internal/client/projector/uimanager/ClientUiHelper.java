package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.core.Assert;
import dev.rico.remoting.Property;
import javafx.scene.control.SelectionModel;

public class ClientUiHelper {

    private ClientUiHelper() {
    }

    public static <T> void bindWithSelectionModel(final Property<T> selectedProperty, final SelectionModel<T> selectionModel) {
        Assert.requireNonNull(selectedProperty, "selectedProperty");
        Assert.requireNonNull(selectionModel, "selectionModel");
        selectionModel.select(selectedProperty.get());
        selectedProperty.onChanged(evt -> {
            if (evt.getNewValue() == null && !selectionModel.isEmpty()) {
                selectionModel.clearSelection();
            } else if (evt.getNewValue() != null && !evt.getNewValue().equals(selectionModel.getSelectedItem())) {
                selectionModel.select(evt.getNewValue());
            }
        });
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedProperty.set(newValue));
    }
}
