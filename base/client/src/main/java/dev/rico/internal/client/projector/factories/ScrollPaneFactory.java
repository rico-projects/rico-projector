package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.ScrollPaneModel;
import javafx.scene.control.ScrollPane;

public class ScrollPaneFactory implements ProjectorNodeFactory<ScrollPaneModel, ScrollPane> {

    @Override
    public ScrollPane create(final Projector projector, final ScrollPaneModel model) {
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        bind(scrollPane.contentProperty()).to(model.contentProperty(), projector::createNode);
        return scrollPane;
    }

    @Override
    public Class<ScrollPaneModel> getSupportedType() {
        return ScrollPaneModel.class;
    }
}
