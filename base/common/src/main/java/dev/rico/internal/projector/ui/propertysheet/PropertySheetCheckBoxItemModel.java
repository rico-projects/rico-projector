package dev.rico.internal.projector.ui.propertysheet;


import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.CheckBoxModel;
import dev.rico.remoting.Property;

@ForRemoval
public class PropertySheetCheckBoxItemModel extends PropertySheetItemModel {
    private Property<CheckBoxModel> field;

    public CheckBoxModel getField() {
        return field.get();
    }

    public void setField(CheckBoxModel field) {
        this.field.set(field);
    }

    public Property<CheckBoxModel> fieldProperty() {
        return field;
    }
}
