package to.remove.uimanager.breadcrumbbar;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import dev.rico.internal.projector.ForRemoval;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.controlsfx.control.SegmentedButton;

import java.util.Collections;

@ForRemoval
public class BreadCrumbBarSkin extends BehaviorSkinBase<SegmentedButton, BehaviorBase<SegmentedButton>> {

    private static final String STYLE_CLASS_FIRST = "first"; //$NON-NLS-1$

    public BreadCrumbBarSkin(final BreadCrumbBar control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding>emptyList()));
        control.toggleGroupProperty().addListener((observable, oldValue, newValue) -> {
            getButtons().forEach((button) -> {
                button.setToggleGroup(newValue);
            });
        });

        updateButtons();
        getButtons().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                updateButtons();
            }
        });

        control.toggleGroupProperty().addListener((observable, oldValue, newValue) -> {
            getButtons().forEach((button) -> {
                button.setToggleGroup(newValue);
            });
        });

        fixFocusTraversal();
    }

    private ObservableList<ToggleButton> getButtons() {
        return getSkinnable().getButtons();
    }


    private void updateButtons() {
        ObservableList<ToggleButton> buttons = getButtons();
        ToggleGroup group = getSkinnable().getToggleGroup();
        getChildren().clear();
        for (int i = 0; i < getButtons().size(); i++) {
            ToggleButton button = buttons.get(i);
            if (group != null) {
                button.setToggleGroup(group);
            }
            getChildren().add(button);
            if (i == 0) {
                if (!button.getStyleClass().contains(BreadCrumbBarSkin.STYLE_CLASS_FIRST)) {
                    button.getStyleClass().add(STYLE_CLASS_FIRST);
                }
            } else {
                button.getStyleClass().remove(STYLE_CLASS_FIRST);
            }
        }
    }

    // https://bitbucket.org/controlsfx/controlsfx/issue/453/breadcrumbbar-keyboard-focus-traversal-is
    // ContainerTabOrder will fail with LEFT/RIGHT navigation, since the buttons
    // in bread crumb overlap
    private void fixFocusTraversal() {

        ParentTraversalEngine engine = new ParentTraversalEngine(getSkinnable(), new Algorithm() {

            @Override
            public Node select(Node owner, Direction dir, TraversalContext context) {
                Node node = null;
                int idx = getChildren().indexOf(owner);
                switch (dir) {
                    case NEXT:
                    case NEXT_IN_LINE:
                    case RIGHT:
                        if (idx < getChildren().size() - 1) {
                            node = getChildren().get(idx + 1);
                        }
                        break;
                    case PREVIOUS:
                    case LEFT:
                        if (idx > 0) {
                            node = getChildren().get(idx - 1);
                        }
                        break;
                }
                return node;
            }

            @Override
            public Node selectFirst(TraversalContext context) {
                Node first = null;
                if (!getChildren().isEmpty()) {
                    first = getChildren().get(0);
                }
                return first;
            }

            @Override
            public Node selectLast(TraversalContext context) {
                Node last = null;
                if (!getChildren().isEmpty()) {
                    last = getChildren().get(getChildren().size() - 1);
                }
                return last;
            }
        });
        engine.setOverriddenFocusTraversability(false);
        getSkinnable().setImpl_traversalEngine(engine);

    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        for (int i = 0; i < getChildren().size(); i++) {
            Node n = getChildren().get(i);

            double nw = snapSize(n.prefWidth(h));
            double nh = snapSize(n.prefHeight(-1));

            if (i > 0) {
                // We have to position the bread crumbs slightly overlapping
                double ins = n instanceof BreadCrumbButton ? ((BreadCrumbButton) n).getArrowWidth() : 0;
                x = snapPosition(x - ins);
            }

            n.resize(nw, nh);
            n.relocate(x, y);
            x += nw;
        }
    }
}



