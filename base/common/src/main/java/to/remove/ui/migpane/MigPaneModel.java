package to.remove.ui.migpane;


import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class MigPaneModel extends ItemListContainerModel<MigPaneItemModel> {
    private ObservableList<MigPaneItemModel> items;
    private Property<String> layoutConstraints;
    private Property<String> columnConstraints;
    private Property<String> rowConstraints;

    public String getLayoutConstraints() {
        return layoutConstraints.get();
    }

    public void setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints.set(layoutConstraints);
    }

    public Property<String> layoutConstraintsProperty() {
        return layoutConstraints;
    }

    public String getRowConstraints() {
        return rowConstraints.get();
    }

    public void setRowConstraints(String rowConstraints) {
        this.rowConstraints.set(rowConstraints);
    }

    public Property<String> rowConstraintsProperty() {
        return rowConstraints;
    }

    public String getColumnConstraints() {
        return columnConstraints.get();
    }

    public void setColumnConstraints(String columnConstraints) {
        this.columnConstraints.set(columnConstraints);
    }

    public Property<String> columnConstraintsProperty() {
        return columnConstraints;
    }

    @Override
    public ObservableList<MigPaneItemModel> getItems() {
        return items;
    }
}
