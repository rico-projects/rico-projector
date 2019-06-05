package dev.rico.internal.projector.ui.propertysheet;


import dev.rico.internal.projector.ui.DateTimeFieldModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class PropertySheetTimeItemModel extends PropertySheetItemModel {
    private Property<DateTimeFieldModel> field;

    public DateTimeFieldModel getField() {
        return field.get();
    }

    public void setField(DateTimeFieldModel field) {
        this.field.set(field);
    }

    public Property<DateTimeFieldModel> fieldProperty() {
        return field;
    }
}
