package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.projector.ui.tabpane.TabPaneModel;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneFactory implements ProjectorNodeFactory<TabPaneModel, TabPane> {

    @Override
    public TabPane create(final Projector projector, final TabPaneModel itemModel) {
        TabPane tabPane = new TabPane();
        // TODO: Tab-Inhalt und Tab-Text sind leider nicht an das jeweilige Model gebunden, sondern nur an den Item
        FXBinder.bind(tabPane.getTabs()).to(itemModel.getItems(), model -> {
            Tab tab = new Tab(model.getCaption(), projector.createNode(model.getContent()));
            tab.setClosable(false);
            return tab;
        });
        FXBinder.bind(tabPane.sideProperty()).to(itemModel.sideProperty(), side -> side == null ? Side.TOP : side);
        return tabPane;
    }

    @Override
    public Class<TabPaneModel> getSupportedType() {
        return TabPaneModel.class;
    }
}
