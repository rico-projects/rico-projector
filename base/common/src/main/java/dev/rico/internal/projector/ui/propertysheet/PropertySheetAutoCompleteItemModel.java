package dev.rico.internal.projector.ui.propertysheet;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.autocompletion.AutoCompleteModel;
import dev.rico.remoting.Property;

@ForRemoval
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
