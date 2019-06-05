package to.remove.ui.propertysheet;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
@ForRemoval
public class PropertySheetTextFieldItemModel extends PropertySheetTextItemModel {
    private Property<TextFieldModel> field;

    public TextFieldModel getField() {
        return field.get();
    }

    public void setField(TextFieldModel field) {
        this.field.set(field);
    }

    public Property<TextFieldModel> textFieldProperty() {
        return field;
    }
}
