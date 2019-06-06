package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.remoting.Property;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import to.remove.ui.SplitMenuButtonModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class SplitMenuButtonFactory implements ProjectorNodeFactory<SplitMenuButtonModel, SplitMenuButton> {
    @Override
    public SplitMenuButton create(Projector projector, SplitMenuButtonModel itemModel) {
        SplitMenuButton button = new SplitMenuButton();
        button.setOnAction(event -> projector.getControllerProxy().invoke(itemModel.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(button.textProperty()).to(itemModel.captionProperty());
        button.getItems().add(createSplitMenuMenuItem(projector, itemModel.actionAProperty(), itemModel.captionAProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, itemModel.actionBProperty(), itemModel.captionBProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, itemModel.actionCProperty(), itemModel.captionCProperty(), button));
        button.getItems().add(createSplitMenuMenuItem(projector, itemModel.actionDProperty(), itemModel.captionDProperty(), button));
        return button;
    }

    private MenuItem createSplitMenuMenuItem(Projector projector, Property<String> actionProperty, Property<String> captionProperty, SplitMenuButton button) {
        MenuItem menuItem = new MenuItem();
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
