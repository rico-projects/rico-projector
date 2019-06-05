package dev.rico.internal.client.projector.uimanager;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.text.HitInfo;
import dev.rico.internal.projector.ForRemoval;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.Objects;

@ForRemoval
public abstract class AutoCompletionFieldSkin extends TextFieldSkin {
    private final TextField control;
    private StackPane rightPane;

    public AutoCompletionFieldSkin(final TextField control) {
        super(control, new TextFieldBehavior(control));

        this.control = control;
        updateChildren();

        registerChangeListener(rightProperty(), "RIGHT_NODE");
    }

    public abstract ObjectProperty<Node> rightProperty();

    @Override
    protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);
        if (Objects.equals(p, "RIGHT_NODE")) {
            updateChildren();
        }
    }

    private void updateChildren() {
        Node newRight = rightProperty().get();
        if (newRight != null) {
            getChildren().remove(rightPane);
            rightPane = new StackPane(newRight);
            rightPane.setManaged(false);
            rightPane.setAlignment(Pos.CENTER_RIGHT);
            getChildren().add(rightPane);
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (rightPane != null) {
            rightPane.resizeRelocate(w - h + 8, 4, h + snappedTopInset() + snappedBottomInset() - 2, h + snappedTopInset() + snappedBottomInset() - 8);
        }
    }

    @Override
    public HitInfo getIndex(double x, double y) {
        final double leftWidth = 0.0;
        return super.getIndex(x - leftWidth, y);
    }

    @Override
    protected double computePrefHeight(double w, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double ph = super.computePrefHeight(w, topInset, rightInset, bottomInset, leftInset);
        final double leftHeight = 0.0;
        final double rightHeight = rightPane == null ? 0.0 : snapSize(rightPane.prefHeight(-1));
        return Math.max(ph, Math.max(leftHeight, rightHeight));
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset);
    }
}
