package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ButtonModel extends ItemModel {
    private Property<String> caption;
    private Property<String> action;
    private Property<String> image;
    private Property<String> tooltip;

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

    public String getTooltip() {
        return tooltip.get();
    }

    public void setTooltip(String tooltip) {
        this.tooltip.set(tooltip);
    }

    public Property<String> tooltipProperty() {
        return tooltip;
    }

    public String getImage() {
        return image.get();
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public Property<String> imageProperty() {
        return image;
    }
}
