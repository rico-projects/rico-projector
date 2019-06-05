package dev.rico.internal.client.projector.uimanager.mixed;

import dev.rico.internal.client.projector.uimanager.mixed.BreadCrumbBar.BreadCrumbActionEvent;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import dev.rico.internal.projector.ForRemoval;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basic Skin implementation for the {@link BreadCrumbBar}
 *
 * @param <T>
 */
@ForRemoval
public class BreadCrumbBarSkin<T> extends BehaviorSkinBase<BreadCrumbBar<T>, BehaviorBase<BreadCrumbBar<T>>> {

  private static final String STYLE_CLASS_FIRST = "first"; //$NON-NLS-1$

  public BreadCrumbBarSkin(final BreadCrumbBar<T> control) {
    super(control, new BehaviorBase<>(control, Collections.<KeyBinding>emptyList()));
    control.selectedCrumbProperty().addListener(selectedPathChangeListener);
    updateSelectedPath(getSkinnable().selectedCrumbProperty().get(), null);
    fixFocusTraversal();
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

  private final ChangeListener<TreeItem<T>> selectedPathChangeListener = (obs, oldItem,
      newItem) -> updateSelectedPath(newItem, oldItem);

  private void updateSelectedPath(TreeItem<T> newTarget, TreeItem<T> oldTarget) {
    if (oldTarget != null) {
      // remove old listener
      oldTarget.removeEventHandler(TreeItem.childrenModificationEvent(), treeChildrenModifiedHandler);
    }
    if (newTarget != null) {
      // add new listener
      newTarget.addEventHandler(TreeItem.childrenModificationEvent(), treeChildrenModifiedHandler);
    }
    updateBreadCrumbs();
  }

  private final EventHandler<TreeModificationEvent<Object>> treeChildrenModifiedHandler = args -> updateBreadCrumbs();

  private void updateBreadCrumbs() {
    final BreadCrumbBar<T> buttonBar = getSkinnable();
    final TreeItem<T> pathTarget = buttonBar.getSelectedCrumb();
    final Callback<TreeItem<T>, ButtonBase> factory = buttonBar.getCrumbFactory();
    final ToggleGroup toggleGroup = new ToggleGroup();
    getChildren().clear();

    if (pathTarget != null) {
      List<TreeItem<T>> crumbs = constructFlatPath(pathTarget);

      for (int i = 0; i < crumbs.size(); i++) {
        ButtonBase crumb = createCrumb(factory, crumbs.get(i));
        toggleGroup.getToggles().add((Toggle) crumb);
        // if (i == 0)
        // toggleGroup.selectToggle((Toggle) crumb);
        crumb.setMnemonicParsing(false);
        if (i == 0) {
          if (!crumb.getStyleClass().contains(BreadCrumbBarSkin.STYLE_CLASS_FIRST)) {
            crumb.getStyleClass().add(BreadCrumbBarSkin.STYLE_CLASS_FIRST);
          }
        }
        else {
          crumb.getStyleClass().remove(BreadCrumbBarSkin.STYLE_CLASS_FIRST);
        }

        getChildren().add(crumb);
      }
    }
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

  /**
   * Construct a flat list for the crumbs
   *
   * @param bottomMost
   *            The crumb node at the end of the path
   * @return
   */
  private List<TreeItem<T>> constructFlatPath(TreeItem<T> bottomMost) {
    List<TreeItem<T>> path = new ArrayList<>();

    TreeItem<T> current = bottomMost;
    do {
      path.add(current);
      current = current.getParent();
    }
    while (current != null);

    Collections.reverse(path);
    return path;
  }

  private ButtonBase createCrumb(final Callback<TreeItem<T>, ButtonBase> factory, final TreeItem<T> selectedCrumb) {

    ButtonBase crumb = factory.call(selectedCrumb);

    crumb.getStyleClass().add("crumb"); //$NON-NLS-1$

    // We want all buttons to have the same height
    // so we bind their preferred height to the enclosing container
    // crumb.prefHeightProperty().bind(getSkinnable().heightProperty());

    // listen to the action event of each bread crumb
    crumb.setOnAction(ae -> onBreadCrumbAction(selectedCrumb));

    return crumb;
  }

  /**
   * Occurs when a bread crumb gets the action event
   *
   * @param crumbModel
   *            The crumb which received the action event
   */
  protected void onBreadCrumbAction(final TreeItem<T> crumbModel) {
    final BreadCrumbBar<T> breadCrumbBar = getSkinnable();

    // fire the composite event in the breadCrumbBar
    Event.fireEvent(breadCrumbBar, new BreadCrumbActionEvent<>(crumbModel));

    // navigate to the clicked crumb
    if (breadCrumbBar.isAutoNavigationEnabled()) {
      breadCrumbBar.setSelectedCrumb(crumbModel);
    }
  }

  /**
   * Represents a BreadCrumb Button
   *
   * <pre>
   * ----------
   *  \         \
   *  /         /
   * ----------
   * </pre>
   *
   *
   */
  public static class BreadCrumbButton<T> extends ToggleButton {

    private final ObjectProperty<Boolean> first = new SimpleObjectProperty<>(this, "first"); //$NON-NLS-1$

    private final double arrowWidth = 5;
    private final double arrowHeight = 20;

    private T value;

    /**
     * Create a BreadCrumbButton
     *
     * @param text
     *            Buttons text
     */
    public BreadCrumbButton(String text, T value) {
      this(text, null, value);
    }

    /**
     * Create a BreadCrumbButton
     *
     * @param text
     *            Buttons text
     * @param gfx
     *            Gfx of the Button
     * @param treeItem
     */
    private BreadCrumbButton(String text, Node gfx, T value) {
      super(text, gfx);
      this.value = value;
      first.set(false);

      getStyleClass().addListener((InvalidationListener) arg0 -> updateShape());

      updateShape();
    }

    public T getValue() {
      return value;
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

    /**
     * Create an arrow path
     *
     * Based upon Uwe / Andy Till code snippet found here:
     *
     * @see http://ustesis.wordpress.com/2013/11/04/implementing-breadcrumbs-in-javafx/
     * @return
     */
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

      if (!getStyleClass().contains(BreadCrumbBarSkin.STYLE_CLASS_FIRST)) {
        // draw lower part of left arrow
        // we simply can omit it for the first Button
        LineTo e6 = new LineTo(arrowWidth, arrowHeight / 2.0);
        path.getElements().add(e6);
      }
      else {
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
}
