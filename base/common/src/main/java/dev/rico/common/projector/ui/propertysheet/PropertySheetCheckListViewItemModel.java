package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.checklistview.CheckListViewModel;
import dev.rico.remoting.Property;

public class PropertySheetCheckListViewItemModel extends PropertySheetItemModel {
    private Property<CheckListViewModel> field;

    public CheckListViewModel getField() {
        return field.get();
    }

    public void setField(CheckListViewModel field) {
        this.field.set(field);
    }

    public Property<CheckListViewModel> fieldProperty() {
        return field;
    }
}