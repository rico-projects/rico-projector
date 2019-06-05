package dev.rico.internal.projector.ui.table;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.time.Instant;

@RemotingBean
@ForRemoval
public class TableInstantCellModel extends TableCellModel<Instant> {
    private Property<Instant> value;

    @Override
    public Instant getValue() {
        return value.get();
    }

    public void setValue(Instant value) {
        this.value.set(value);
    }

    public Property<Instant> valueProperty() {
        return value;
    }
}
