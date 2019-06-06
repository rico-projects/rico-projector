package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ToolBarModel;
import javafx.scene.control.ToolBar;

import static dev.rico.client.remoting.FXBinder.bind;

public class ToolBarFactory implements ProjectorNodeFactory<ToolBarModel, ToolBar> {

    @Override
    public ToolBar create(final Projector projector, final ToolBarModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        ToolBar toolBar = new ToolBar();
        bind(toolBar.getItems()).to(model.getItems(), projector::createNode);
        return toolBar;
    }

    @Override
    public Class<ToolBarModel> getSupportedType() {
        return ToolBarModel.class;
    }
}
