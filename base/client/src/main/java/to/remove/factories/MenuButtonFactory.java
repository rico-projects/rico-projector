package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.factories.ActionHandlerFactory;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.scene.control.MenuItem;
import to.remove.uimanager.MenuButton;
import javafx.scene.image.ImageView;
import to.remove.Image;
import to.remove.ui.menubutton.MenuButtonItemModel;
import to.remove.ui.menubutton.MenuButtonModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class MenuButtonFactory implements ProjectorNodeFactory<MenuButtonModel, MenuButton>, ActionHandlerFactory {

    @Override
    public MenuButton create(final Projector projector, final MenuButtonModel model) {
        Assert.requireNonNull(model, "model");

        final MenuButton button = new MenuButton();
        if (model.getCaption() == null) {
            button.getStyleClass().add("activeButton");
            button.setGraphic(new ImageView(Image.COG));
        } else {
            bind(button.textProperty()).to(model.captionProperty());
        }
        bind(button.getItems()).to(model.getItems(), v -> createMenuItem(projector, v));
        return button;
    }

    private MenuItem createMenuItem(final Projector projector, MenuButtonItemModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ControllerProxy<? extends ManagedUiModel> controllerProxy = projector.getControllerProxy();
        Assert.requireNonNull(controllerProxy, "controllerProxy");

        final MenuItem menuItem = new MenuItem();
        bind(menuItem.textProperty()).to(model.captionProperty());
        if (model.getAction() != null) {
            menuItem.setOnAction(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(null, throwable)));
        } else {
            menuItem.setOnAction(createOnActionHandler("buttonClick", model, projector));
        }
        return menuItem;
    }

    @Override
    public Class<MenuButtonModel> getSupportedType() {
        return MenuButtonModel.class;
    }
}
