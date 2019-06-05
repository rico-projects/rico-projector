package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.PaxCodeFieldModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class PropertySheetPaxCodeItemModel extends PropertySheetItemModel {
    private Property<PaxCodeFieldModel> field;
    private Property<String> value;

    public Property<String> valueProperty() {
        return value;
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public PaxCodeFieldModel getField() {
        return field.get();
    }

    public void setField(PaxCodeFieldModel field) {
        this.field.set(field);
    }

    public Property<PaxCodeFieldModel> fieldProperty() {
        return field;
    }
}
