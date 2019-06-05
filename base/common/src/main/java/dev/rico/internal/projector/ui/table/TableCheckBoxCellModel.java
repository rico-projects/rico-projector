package dev.rico.internal.projector.ui.table;

import dev.rico.remoting.Property;

public class TableCheckBoxCellModel extends TableCellModel<Boolean> {
    private Property<Boolean> value;

    public Property<Boolean> valueProperty() {
        return value;
    }
}
