package to.remove.ui.propertysheet;


import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.remoting.Property;
@ForRemoval
public class PropertySheetTextAreaItemModel extends PropertySheetTextItemModel {
    private Property<TextAreaModel> field;

    public TextAreaModel getField() {
        return field.get();
    }

    public void setField(TextAreaModel field) {
        this.field.set(field);
    }

    public Property<TextAreaModel> fieldProperty() {
        return field;
    }
}
