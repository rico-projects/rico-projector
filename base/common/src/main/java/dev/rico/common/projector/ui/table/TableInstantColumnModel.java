package dev.rico.common.projector.ui.table;

import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableInstantColumnModel extends TableColumnModel {
    private Property<String> formatString;

    public String getFormatString() {
        return formatString.get();
    }

    public Property<String> formatStringProperty() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString.set(formatString);
    }
}
