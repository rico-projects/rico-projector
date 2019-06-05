package dev.rico.internal.projector.ui.table;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@SuppressWarnings("ClassWithoutLogger")
@RemotingBean
public class TableColumnModel extends IdentifiableModel {
    private ObservableList<TableColumnModel> children;
    private Property<String> caption;
    private Property<Boolean> editable;
    private Property<Double> prefWidth;

    public Boolean getEditable() {
        return editable.get();
    }

    public void setEditable(Boolean editable) {
        this.editable.set(editable);
    }

    public Property<Boolean> editableProperty() {
        return editable;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public Double getPrefWidth() {
        return prefWidth.get();
    }

    public void setPrefWidth(Double prefWidth) {
        this.prefWidth.set(prefWidth);
    }

    public Property<Double> prefWidthProperty() {
        return prefWidth;
    }

    public ObservableList<TableColumnModel> getChildren() {
        return children;
    }
}
