package dev.rico.internal.projector.ui.gridpane;


import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.HPos;
import javafx.scene.layout.Priority;

@RemotingBean
public class GridPaneColumnConstraintsModel extends IdentifiableModel {
    private Property<Boolean> fillWidth;
    private Property<HPos> hAlignment;
    private Property<Priority> hGrow;
    private Property<Double> minWidth;
    private Property<Double> prefWidth;
    private Property<Double> maxWidth;
    private Property<Double> percentWidth;

    public Boolean getFillWidth() {
        return fillWidth.get();
    }

    public void sethAlignment(Boolean fillWidth) {
        this.fillWidth.set(fillWidth);
    }

    public Property<Boolean> fillWidthProperty() {
        return fillWidth;
    }

    public HPos getHalignment() {
        return hAlignment.get();
    }

    public void setHalignment(HPos hAlignment) {
        this.hAlignment.set(hAlignment);
    }

    public Property<HPos> halignmentProperty() {
        return hAlignment;
    }

    public Priority getHgrow() {
        return hGrow.get();
    }

    public void setHgrow(Priority hGrow) {
        this.hGrow.set(hGrow);
    }

    public Property<Priority> hgrowProperty() {
        return hGrow;
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

    public Double getMaxWidth() {
        return maxWidth.get();
    }

    public void setMaxWidth(Double maxWidth) {
        this.maxWidth.set(maxWidth);
    }

    public Property<Double> maxWidthProperty() {
        return maxWidth;
    }

    public Double getMinWidth() {
        return minWidth.get();
    }

    public void setMinWidth(Double minWidth) {
        this.minWidth.set(minWidth);
    }

    public Property<Double> minWidthProperty() {
        return minWidth;
    }

    public Double getPercentWidth() {
        return percentWidth.get();
    }

    public void setPercentWidth(Double percentWidth) {
        this.percentWidth.set(percentWidth);
    }

    public Property<Double> minPercentProperty() {
        return percentWidth;
    }
}
