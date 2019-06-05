package dev.rico.common.projector.ui.checklistview;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.listselectionview.ListSelectionViewItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class CheckListViewModel extends ItemModel {
    private ObservableList<ListSelectionViewItemModel> availableItems;
    private ObservableList<ListSelectionViewItemModel> selectedItems;
    private Property<Boolean> editable;

    public ObservableList<ListSelectionViewItemModel> getAvailableItems() {
        return availableItems;
    }

    public ObservableList<ListSelectionViewItemModel> getSelectedItems() {
        return selectedItems;
    }

    public Boolean getEditable() {
        return editable.get();
    }

    public void setEditable(Boolean editable) {
        this.editable.set(editable);
    }

    public Property<Boolean> editableProperty() {
        return editable;
    }
}
