package dev.rico.internal.projector.ui;


import dev.rico.projector.model.WithText;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

@RemotingBean
public class LabelModel extends ItemModel<LabelModel> implements WithText<LabelModel> {

    private Property<String> text;

    private Property<Pos> alignment;

    private Property<Boolean> wrapText;

    private Property<TextAlignment> textAlignment;

    public Property<String> textProperty() {
        return text;
    }

    public Boolean getWrapText() {
        return wrapText.get();
    }

    public void setWrapText(Boolean wrapText) {
        this.wrapText.set(wrapText);
    }

    public Property<Boolean> wrapTextProperty() {
        return wrapText;
    }

    public TextAlignment getTextAlignment() {
        return textAlignment.get();
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment.set(textAlignment);
    }

    public Property<TextAlignment> textAlignmentProperty() {
        return textAlignment;
    }

    public Pos getAlignment() {
        return alignment.get();
    }

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public Property<Pos> alignmentProperty() {
        return alignment;
    }
}
