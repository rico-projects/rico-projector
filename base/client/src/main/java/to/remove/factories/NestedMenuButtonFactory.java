package to.remove.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.projector.Projector;
import dev.rico.internal.client.projector.factories.ActionHandlerFactory;
import dev.rico.internal.client.projector.factories.ButtonBaseFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import to.remove.ui.nestedmenubutton.NestedMenuButtonModel;
import to.remove.uimanager.MenuButton;

public class NestedMenuButtonFactory extends ButtonBaseFactory<NestedMenuButtonModel, MenuButton> implements ActionHandlerFactory {

    @Override
    public MenuButton create(final Projector projector, final NestedMenuButtonModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final MenuButton button = new MenuButton();
        configureButton(model, button);
        bind(button.textProperty()).to(model.captionProperty());
        bind(button.getItems()).to(model.getItems(), v -> createNestedMenuItem(projector, v));
        return button;
    }

    private MenuItem createNestedMenuItem(final Projector projector, final MenuItemModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final PostProcessor postProcessor = projector.getPostProcessor();
        Assert.requireNonNull(postProcessor, "postProcessor");

        if (model.getItems().isEmpty()) {
            final MenuItem menuItem = new MenuItem();
            bind(menuItem.graphicProperty()).to(model.graphicProperty(), v -> projector.createNode(v));
            bind(menuItem.textProperty()).to(model.captionProperty());
            menuItem.setOnAction(event -> {
                if (event.getTarget() == menuItem) {
                    event.consume();
                    createOnActionHandler("buttonClick", model, projector).handle(event);
                }
            });
            postProcessor.postProcess(model.getId(), model, menuItem);
            return menuItem;
        } else {
            final Menu menu = new Menu();
            menu.setOnAction(event -> {
                if (event.getTarget() == menu) {
                    event.consume();
                    menu.getParentPopup().hide();
                    createOnActionHandler("buttonClick", model, projector).handle(event);
                }
            });
            bind(menu.graphicProperty()).to(model.graphicProperty(), v -> projector.createNode(v));
            bind(menu.textProperty()).to(model.captionProperty());
            bind(menu.getItems()).to(model.getItems(), v -> createNestedMenuItem(projector, v));
            postProcessor.postProcess(model.getId(), model, menu);
            return menu;
        }
    }

    @Override
    public Class<NestedMenuButtonModel> getSupportedType() {
        return NestedMenuButtonModel.class;
    }
}
