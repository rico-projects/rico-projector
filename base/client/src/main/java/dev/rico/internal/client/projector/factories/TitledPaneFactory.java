package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.TitledPaneModel;
import javafx.scene.control.TitledPane;

import static dev.rico.client.remoting.FXBinder.bind;

public class TitledPaneFactory implements ProjectorNodeFactory<TitledPaneModel, TitledPane> {
    @Override
    public TitledPane create(Projector projector, TitledPaneModel model) {
        TitledPane titledPane = new TitledPane();
        bind(titledPane.textProperty()).to(model.titleProperty());
        bind(titledPane.contentProperty()).to(model.contentProperty(), projector::createNode);
        return titledPane;
    }

    @Override
    public Class<TitledPaneModel> getSupportedType() {
        return TitledPaneModel.class;
    }
}
