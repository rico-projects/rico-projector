package dev.rico.internal.client.projector.uimanager;

import dev.rico.remoting.Property;
import javafx.scene.control.SelectionModel;

public class ClientUiHelper {
   static <T> void bindWithSelectionModel(Property<T> selectedProperty, SelectionModel<T> selectionModel) {
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
