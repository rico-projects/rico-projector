package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.DateTimeFieldModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.time.Instant;

@RemotingBean
public class PropertySheetInstantItemModel extends PropertySheetItemModel {
    private Property<DateTimeFieldModel> field;
    private Property<Instant> value;

    public Property<Instant> valueProperty() {
        return value;
    }

    public Instant getValue() {
        return value.get();
    }

    public void setValue(Instant value) {
        this.value.set(value);
    }

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
