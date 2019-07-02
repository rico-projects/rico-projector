package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.tabpane.TabPaneModel;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneFactory implements ProjectorNodeFactory<TabPaneModel, TabPane> {

    @Override
    public TabPane create(final Projector projector, final TabPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");
        final TabPane tabPane = new TabPane();
        FXBinder.bind(tabPane.getTabs()).to(model.getItems(), m -> {
            final Tab tab = new Tab(m.getCaption(), projector.createNode(m.getContent()));
            tab.setClosable(false);
            return tab;
        });
        FXBinder.bind(tabPane.sideProperty()).to(model.sideProperty(), side -> side == null ? Side.TOP : side);
        return tabPane;
    }

    @Override
    public Class<TabPaneModel> getSupportedType() {
        return TabPaneModel.class;
    }
}
