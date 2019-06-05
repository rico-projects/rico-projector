package dev.rico.internal.client.projector.uimanager.mixed;

import com.sun.javafx.event.EventHandlerManager;

import dev.rico.internal.client.projector.uimanager.mixed.BreadCrumbBarSkin.BreadCrumbButton;
import dev.rico.internal.projector.ForRemoval;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

/**
 * Represents a bread crumb bar. This control is useful to visualize and
 * navigate a hierarchical path structure, such as file systems.
 *
 * <p>
 * Shown below is a screenshot of the BreadCrumbBar control:
 *
 * <br>
 * <center> <img src="breadCrumbBar.png" alt="Screenshot of BreadCrumbBar">
 * </center>
 */
@ForRemoval
public class BreadCrumbBar<T> extends Control {

	private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);

	/**
	 * Represents an Event which is fired when a bread crumb was activated.
	 */
	@SuppressWarnings("serial")
	public static class BreadCrumbActionEvent<TE> extends Event {

		/**
		 * The event type that should be listened to by people interested in
		 * knowing when the {@link BreadCrumbBar#selectedCrumbProperty()
		 * selected crumb} has changed.
		 */
		@SuppressWarnings("rawtypes")
		public static final EventType<BreadCrumbActionEvent> CRUMB_ACTION = new EventType<>("CRUMB_ACTION"); //$NON-NLS-1$

		private final TreeItem<TE> selectedCrumb;

		/**
		 * Creates a new event that can subsequently be fired.
		 */
		public BreadCrumbActionEvent(TreeItem<TE> selectedCrumb) {
			super(CRUMB_ACTION);
			this.selectedCrumb = selectedCrumb;
		}

		/**
		 * Returns the crumb which was the action target.
		 */
		public TreeItem<TE> getSelectedCrumb() {
			return selectedCrumb;
		}
	}

	/**
	 * Construct a tree model from the flat list which then can be set as
	 * selectedCrumb node to be shown
	 *
	 * @param crumbs
	 */
	public static <T> TreeItem<T> buildTreeModel(@SuppressWarnings("unchecked") T... crumbs) {
		TreeItem<T> subRoot = null;
		for (T crumb : crumbs) {
			TreeItem<T> currentNode = new TreeItem<>(crumb);
			if (subRoot == null) {
				subRoot = currentNode;
			} else {
				subRoot.getChildren().add(currentNode);
				subRoot = currentNode;
			}
		}
		return subRoot;
	}

	/***************************************************************************
	 *
	 * Private fields
	 *
	 **************************************************************************/

	/**
	 * Default crumb node factory. This factory is used when no custom factory
	 * is specified by the user.
	 */
	private final Callback<TreeItem<T>, ButtonBase> defaultCrumbNodeFactory = new Callback<TreeItem<T>, ButtonBase>() {
		@Override
		public ButtonBase call(TreeItem<T> crumb) {
			return new BreadCrumbBarSkin.BreadCrumbButton<>(crumb.getValue() != null ? crumb.getValue().toString() : "", //$NON-NLS-1$
					crumb.getValue());
		}
	};

	/***************************************************************************
	 * * Constructors * *
	 **************************************************************************/

	/**
	 * Creates an empty bread crumb bar
	 */
	public BreadCrumbBar() {
		this(null);
	}

	/**
	 * Creates a bread crumb bar with the given TreeItem as the currently
	 * selected crumb.
	 */
	public BreadCrumbBar(TreeItem<T> selectedCrumb) {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		setSelectedCrumb(selectedCrumb);
		setCrumbFactory(defaultCrumbNodeFactory);
	}

	/***************************************************************************
	 * * Public API * *
	 **************************************************************************/

	/** {@inheritDoc} */
	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return tail.prepend(eventHandlerManager);
	}

	/***************************************************************************
	 * * Properties * *
	 **************************************************************************/

	// --- selectedCrumb
	/**
	 * Represents the bottom-most path node (the node on the most-right side in
	 * terms of the bread crumb bar). The full path is then being constructed
	 * using getParent() of the tree-items.
	 *
	 * <p>
	 * Consider the following hierarchy: [Root] &gt; [Folder] &gt; [SubFolder]
	 * &gt; [myfile.txt]
	 *
	 * To show the above bread crumb bar, you have to set the [myfile.txt]
	 * tree-node as selected crumb.
	 */
	public final ObjectProperty<TreeItem<T>> selectedCrumbProperty() {
		return selectedCrumb;
	}

	private final ObjectProperty<TreeItem<T>> selectedCrumb = new SimpleObjectProperty<>(this, "selectedCrumb"); //$NON-NLS-1$

	/**
	 * Get the current target path
	 */
	public final TreeItem<T> getSelectedCrumb() {
		return selectedCrumb.get();
	}

	/**
	 * Select one node in the BreadCrumbBar for being the bottom-most path node.
	 *
	 * @param selectedCrumb
	 */
	public final void setSelectedCrumb(TreeItem<T> selectedCrumb) {
		this.selectedCrumb.set(selectedCrumb);
	}

	// --- autoNavigation
	/**
	 * Enable or disable auto navigation (default is enabled). If auto
	 * navigation is enabled, it will automatically navigate to the crumb which
	 * was clicked by the user.
	 *
	 * @return a {@link BooleanProperty}
	 */
	public final BooleanProperty autoNavigationEnabledProperty() {
		return autoNavigation;
	}

	private final BooleanProperty autoNavigation = new SimpleBooleanProperty(this, "autoNavigationEnabled", true); //$NON-NLS-1$

	/**
	 * Return whether auto-navigation is enabled.
	 *
	 * @return whether auto-navigation is enabled.
	 */
	public final boolean isAutoNavigationEnabled() {
		return autoNavigation.get();
	}

	/**
	 * Enable or disable auto navigation (default is enabled). If auto
	 * navigation is enabled, it will automatically navigate to the crumb which
	 * was clicked by the user.
	 *
	 * @param enabled
	 */
	public final void setAutoNavigationEnabled(boolean enabled) {
		autoNavigation.set(enabled);
	}

	// --- crumbFactory
	/**
	 * Return an ObjectProperty of the CrumbFactory.
	 *
	 * @return an ObjectProperty of the CrumbFactory.
	 */
	public final ObjectProperty<Callback<TreeItem<T>, ButtonBase>> crumbFactoryProperty() {
		return crumbFactory;
	}

	private final ObjectProperty<Callback<TreeItem<T>, ButtonBase>> crumbFactory = new SimpleObjectProperty<>(this,
			"crumbFactory"); //$NON-NLS-1$

	/**
	 * Sets the crumb factory to create (custom) {@link BreadCrumbButton}
	 * instances. <code>null</code> is not allowed and will result in a fall
	 * back to the default factory.
	 *
	 * @param value
	 */
	public final void setCrumbFactory(Callback<TreeItem<T>, ButtonBase> value) {
		if (value == null) {
			value = defaultCrumbNodeFactory;
		}
		crumbFactoryProperty().set(value);
	}

	/**
	 * Returns the cell factory that will be used to create
	 * {@link BreadCrumbButton} instances
	 */
	public final Callback<TreeItem<T>, ButtonBase> getCrumbFactory() {
		return crumbFactory.get();
	}

	// --- onCrumbAction
	/**
	 * @return an ObjectProperty representing the crumbAction EventHandler being
	 *         used.
	 */
	public final ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbActionProperty() {
		return onCrumbAction;
	}

	/**
	 * Set a new EventHandler for when a user selects a crumb.
	 *
	 * @param value
	 */
	public final void setOnCrumbAction(EventHandler<BreadCrumbActionEvent<T>> value) {
		onCrumbActionProperty().set(value);
	}

	/**
	 * Return the EventHandler currently used when a user selects a crumb.
	 *
	 * @return the EventHandler currently used when a user selects a crumb.
	 */
	public final EventHandler<BreadCrumbActionEvent<T>> getOnCrumbAction() {
		return onCrumbActionProperty().get();
	}

	private ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbAction = new ObjectPropertyBase<EventHandler<BreadCrumbBar.BreadCrumbActionEvent<T>>>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected void invalidated() {
			eventHandlerManager.setEventHandler(BreadCrumbActionEvent.CRUMB_ACTION,
					(EventHandler<BreadCrumbActionEvent>) (Object) get());
		}

		@Override
		public Object getBean() {
			return BreadCrumbBar.this;
		}

		@Override
		public String getName() {
			return "onCrumbAction"; //$NON-NLS-1$
		}
	};

	/***************************************************************************
	 * * Stylesheet Handling * *
	 **************************************************************************/

	private static final String DEFAULT_STYLE_CLASS = "bread-crumb-bar"; //$NON-NLS-1$

	/** {@inheritDoc} */
	@Override
	protected Skin<?> createDefaultSkin() {
		return new BreadCrumbBarSkin<T>(this);
	}

	/** {@inheritDoc} */
	// @Override public String getUserAgentStylesheet() {
	// return getUserAgentStylesheet(BreadCrumbBar.class, "breadcrumbbar.css");
	// }
}
