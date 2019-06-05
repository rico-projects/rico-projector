package dev.rico.internal.client.projector.uimanager.breadcrumbbar;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class BreadCrumbButton extends ToggleButton {

    private final ObjectProperty<Boolean> first = new SimpleObjectProperty<>(this, "first"); //$NON-NLS-1$

    private final double arrowWidth = 5;
    private final double arrowHeight = 20;

    BreadCrumbButton(String text) {
        this(text, null);
    }

    private BreadCrumbButton(String text, Node gfx) {
        super(text, gfx);
        first.set(false);

        getStyleClass().addListener((InvalidationListener) arg0 -> updateShape());

        updateShape();
    }

    private void updateShape() {
        setShape(createButtonShape());
    }

    @Override
    public void fire() {
        // we don't toggle from selected to not selected if part of a group
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }

    /**
     * Gets the crumb arrow with
     *
     * @return
     */
    public double getArrowWidth() {
        return arrowWidth;
    }

    private static final String STYLE_CLASS_FIRST = "first"; //$NON-NLS-1$

    private Path createButtonShape() {
        // build the following shape (or home without left arrow)

        // --------
        // \ \
        // / /
        // --------
        Path path = new Path();

        // begin in the upper left corner
        MoveTo e1 = new MoveTo(0, 0);
        path.getElements().add(e1);

        // draw a horizontal line that defines the width of the shape
        HLineTo e2 = new HLineTo();
        // bind the width of the shape to the width of the button
        e2.xProperty().bind(widthProperty().subtract(arrowWidth));
        path.getElements().add(e2);

        // draw upper part of right arrow
        LineTo e3 = new LineTo();
        // the x endpoint of this line depends on the x property of line e2
        e3.xProperty().bind(e2.xProperty().add(arrowWidth));
        e3.setY(arrowHeight / 2.0);
        path.getElements().add(e3);

        // draw lower part of right arrow
        LineTo e4 = new LineTo();
        // the x endpoint of this line depends on the x property of line e2
        e4.xProperty().bind(e2.xProperty());
        e4.setY(arrowHeight);
        path.getElements().add(e4);

        // draw lower horizontal line
        HLineTo e5 = new HLineTo(0);
        path.getElements().add(e5);

        if (!getStyleClass().contains(STYLE_CLASS_FIRST)) {
            // draw lower part of left arrow
            // we simply can omit it for the first Button
            LineTo e6 = new LineTo(arrowWidth, arrowHeight / 2.0);
            path.getElements().add(e6);
        } else {
            // draw an arc for the first bread crumb
            // ArcTo arcTo = new ArcTo();
            // arcTo.setSweepFlag(true);
            // arcTo.setX(0);
            // arcTo.setY(0);
            // arcTo.setRadiusX(15.0f);
            // arcTo.setRadiusY(15.0f);
            // path.getElements().add(arcTo);
        }

        // close path
        ClosePath e7 = new ClosePath();
        path.getElements().add(e7);
        // this is a dummy color to fill the shape, it won't be visible
        path.setFill(Color.BLACK);

        return path;
    }
}