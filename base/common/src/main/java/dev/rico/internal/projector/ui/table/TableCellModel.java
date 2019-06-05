package dev.rico.internal.projector.ui.table;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;

public abstract class TableCellModel<T> extends IdentifiableModel {

    public T getValue() {
        return valueProperty().get();
    }

    public void setValue(T value) {
        valueProperty().set(value);
    }

    abstract public Property<T> valueProperty();
}
