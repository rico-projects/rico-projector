package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.remoting.Property;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import to.remove.ui.SplitMenuButtonModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class SplitMenuButtonFactory implements ProjectorNodeFactory<SplitMenuButtonModel, SplitMenuButton> {

    @Override
    public SplitMenuButton create(final Projector projector, final SplitMenuButtonModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final SplitMenuButton button = new SplitMenuButton();
        button.setOnAction(event -> projector.getControllerProxy().invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(button.textProperty()).to(model.captionProperty());
        button.getItems().add(createSplitMenuMenuItem(projector, model.actionAProperty(), model.captionAProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, model.actionBProperty(), model.captionBProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, model.actionCProperty(), model.captionCProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, model.actionDProperty(), model.captionDProperty(), button));
        return button;
    }

    private MenuItem createSplitMenuMenuItem(final Projector projector, final Property<String> actionProperty, final Property<String> captionProperty, final SplitMenuButton button) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(actionProperty, "actionProperty");

        final MenuItem menuItem = new MenuItem();
        menuItem.setVisible(actionProperty.get() != null);
        actionProperty.onChanged(evt -> menuItem.setVisible(evt.getNewValue() != null));
        menuItem.setOnAction(event -> projector.getControllerProxy().invoke(actionProperty.get()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(menuItem.textProperty()).to(captionProperty);
        return menuItem;
    }

    @Override
    public Class<SplitMenuButtonModel> getSupportedType() {
        return null;
    }
}
