package dev.rico.common.projector.ui.gridpane;


import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;

@RemotingBean
public class GridPaneItemModel extends IdentifiableModel {
    private Property<Integer> col;
    private Property<Integer> row;
    private Property<Integer> colSpan;
    private Property<Integer> rowSpan;
    private Property<HPos> hAlignment;
    private Property<VPos> vAlignment;
    private Property<Priority> hGrow;
    private Property<Priority> vGrow;
    private Property<ItemModel> item;

    public Integer getCol() {
        return col.get();
    }

    public void setCol(Integer col) {
        this.col.set(col);
    }

    public Property<Integer> colProperty() {
        return col;
    }

    public Integer getRow() {
        return row.get();
    }

    public void setRow(Integer row) {
        this.row.set(row);
    }

    public Property<Integer> rowProperty() {
        return row;
    }

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }

    public Integer getColSpan() {
        return colSpan.get();
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan.set(colSpan);
    }

    public Property<Integer> colSpanProperty() {
        return colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan.get();
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan.set(rowSpan);
    }

    public Property<Integer> rowSpanProperty() {
        return rowSpan;
    }

    public HPos gethAlignment() {
        return hAlignment.get();
    }

    public void sethAlignment(HPos hAlignment) {
        this.hAlignment.set(hAlignment);
    }

    public Property<HPos> hAlignmentProperty() {
        return hAlignment;
    }

    public VPos getvAlignment() {
        return vAlignment.get();
    }

    public void setvAlignment(VPos vAlignment) {
        this.vAlignment.set(vAlignment);
    }

    public Property<VPos> vAlignmentProperty() {
        return vAlignment;
    }

    public Priority gethGrow() {
        return hGrow.get();
    }

    public void sethGrow(Priority hGrow) {
        this.hGrow.set(hGrow);
    }

    public Property<Priority> hGrowProperty() {
        return hGrow;
    }

    public Priority getvGrow() {
        return vGrow.get();
    }

    public void setvGrow(Priority vGrow) {
        this.vGrow.set(vGrow);
    }

    public Property<Priority> vGrowProperty() {
        return vGrow;
    }
}
