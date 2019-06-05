package dev.rico.internal.projector.ui.propertysheet;


import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.FuelFieldModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
@ForRemoval
public class PropertySheetFuelItemModel extends PropertySheetItemModel{
    private Property<FuelFieldModel> field;

    public FuelFieldModel getField() {
        return field.get();
    }

    public void setField(FuelFieldModel field) {
        this.field.set(field);
    }

    public Property<FuelFieldModel> fieldProperty() {
        return field;
    }
}
