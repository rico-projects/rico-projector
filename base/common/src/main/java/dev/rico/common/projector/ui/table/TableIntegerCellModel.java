package dev.rico.common.projector.ui.table;

import dev.rico.remoting.Property;

public class TableIntegerCellModel extends TableCellModel<Integer> {
    private Property<Integer> value;

    public Property<Integer> valueProperty() {
        return value;
    }
}
