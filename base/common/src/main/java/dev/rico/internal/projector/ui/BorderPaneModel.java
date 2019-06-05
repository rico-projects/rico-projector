package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class BorderPaneModel extends ItemModel  implements WithPadding{
    private Property<ItemModel> top;
    private Property<ItemModel> left;
    private Property<ItemModel> center;
    private Property<ItemModel> right;
    private Property<ItemModel> bottom;
    private Property<Integer> padding;

    public ItemModel getTop() {
        return top.get();
    }

    public void setTop(ItemModel top) {
        this.top.set(top);
    }

    public Property<ItemModel> topProperty() {
        return top;
    }

    public ItemModel getLeft() {
        return left.get();
    }

    public void setLeft(ItemModel left) {
        this.left.set(left);
    }

    public Property<ItemModel> leftProperty() {
        return left;
    }

    public ItemModel getCenter() {
        return center.get();
    }

    public void setCenter(ItemModel center) {
        this.center.set(center);
    }

    public Property<ItemModel> centerProperty() {
        return center;
    }

    public ItemModel getRight() {
        return right.get();
    }

    public void setRight(ItemModel right) {
        this.right.set(right);
    }

    public Property<ItemModel> rightProperty() {
        return right;
    }

    public ItemModel getBottom() {
        return bottom.get();
    }

    public void setBottom(ItemModel bottom) {
        this.bottom.set(bottom);
    }

    public Property<ItemModel> bottomProperty() {
        return bottom;
    }

    public Integer getPadding() {
        return padding.get();
    }

    public Property<Integer> paddingProperty() {
        return padding;
    }

    public void setPadding(Integer padding) {
        this.padding.set(padding);
    }
}
