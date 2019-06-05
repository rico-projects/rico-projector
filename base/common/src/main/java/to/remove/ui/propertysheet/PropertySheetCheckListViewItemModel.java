package to.remove.ui.propertysheet;


import dev.rico.internal.projector.ForRemoval;
import to.remove.ui.checklistview.CheckListViewModel;
import dev.rico.remoting.Property;

@ForRemoval
public class PropertySheetCheckListViewItemModel extends PropertySheetItemModel {
    private Property<CheckListViewModel> field;

    public CheckListViewModel getField() {
        return field.get();
    }

    public void setField(CheckListViewModel field) {
        this.field.set(field);
    }

    public Property<CheckListViewModel> fieldProperty() {
        return field;
    }
}
