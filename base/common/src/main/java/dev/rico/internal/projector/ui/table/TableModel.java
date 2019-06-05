package dev.rico.internal.projector.ui.table;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableModel extends ItemModel {
    private ObservableList<TableColumnModel> columns;
    private ObservableList<TableRowModel> rows;
    private Property<TableRowModel> selectedRow;
    private Property<Boolean> editable;
    private Property<ItemModel> placeholder;

    public ObservableList<TableColumnModel> getColumns() {
        return columns;
    }

    public ObservableList<TableRowModel> getRows() {
        return rows;
    }

    public TableRowModel getSelectedRow() {
        return selectedRow.get();
    }

    public void setSelectedRow(TableRowModel selectedRow) {
        this.selectedRow.set(selectedRow);
    }

    public Property<TableRowModel> selectedRowProperty() {
        return selectedRow;
    }

    public ItemModel getPlaceholder() {
        return placeholder.get();
    }

    public Property<ItemModel> placeholderProperty() {
        return placeholder;
    }

    public void setPlaceholder(ItemModel placeholder) {
        this.placeholder.set(placeholder);
    }

    public Boolean getEditable() {
        return editable.get();
    }

    public void setEditable(Boolean editable) {
        this.editable.set(editable);
    }

    public Property<Boolean> editableProperty() {
        return editable;
    }
}
