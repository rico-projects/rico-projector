package dev.rico.common.projector.ui.propertysheet;

import dev.rico.common.projector.ui.autocompletion.AutoCompleteModel;
import dev.rico.remoting.Property;

public class PropertySheetAutoCompleteItemModel extends PropertySheetItemModel{
    private Property<AutoCompleteModel> field;

    public AutoCompleteModel getField() {
        return field.get();
    }

    public void setField(AutoCompleteModel field) {
        this.field.set(field);
    }

    public Property<AutoCompleteModel> fieldProperty() {
        return field;
    }
}
