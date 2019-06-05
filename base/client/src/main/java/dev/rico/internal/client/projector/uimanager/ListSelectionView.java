package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ui.listselectionview.ListSelectionViewItemModel;
import dev.rico.internal.projector.ui.listselectionview.ListSelectionViewModel;
import dev.rico.client.remoting.FXBinder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;

class ListSelectionView extends org.controlsfx.control.ListSelectionView<ListSelectionViewItemModel> {
    private BooleanProperty editable;

    public ListSelectionView(ListSelectionViewModel itemModel) {
        setSourceHeader(new Label("Verfügbare Personen"));
        setTargetHeader(new Label("Paxe für diesen Flug"));
        FXBinder.bind(getSourceItems()).bidirectionalTo(itemModel.getAvailableItems());
        FXBinder.bind(getTargetItems()).bidirectionalTo(itemModel.getSelectedItems());
        FXBinder.bind(itemModel.editableProperty()).bidirectionalTo(editableProperty());
        setCellFactory(list -> new ListSelectionViewItemCell());
    }

    @Override
    protected Skin<org.controlsfx.control.ListSelectionView<ListSelectionViewItemModel>> createDefaultSkin() {
        return new ListSelectionViewSkin<>(this);
    }

    public final boolean isEditable() {
        return editable == null || editable.get();
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(false);
        }
        return editable;
    }

    static class ListSelectionViewItemCell extends ListCell<ListSelectionViewItemModel> {
        @Override
        public void updateItem(ListSelectionViewItemModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getCaption());
            } else setText("");
        }
    }
}
