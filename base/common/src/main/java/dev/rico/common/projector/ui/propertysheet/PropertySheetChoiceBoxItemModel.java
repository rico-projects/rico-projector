package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class PropertySheetChoiceBoxItemModel extends PropertySheetItemModel{
    private Property<ChoiceBoxModel> field;

    public ChoiceBoxModel getField() {
        return field.get();
    }

    public void setField(ChoiceBoxModel field) {
        this.field.set(field);
    }

    public Property<ChoiceBoxModel> fieldProperty() {
        return field;
    }
}
