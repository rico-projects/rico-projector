package dev.rico.internal.projector.ui.table;

import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableStringCellModel extends TableCellModel<String> {
    private Property<String> value;

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public Property<String> valueProperty() {
        return value;
    }
}
