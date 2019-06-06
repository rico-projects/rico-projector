package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.client.projector.uimanager.IndexedJavaFXListBinder;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneModel;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;

import java.util.Optional;

import static dev.rico.client.remoting.FXBinder.bind;

public class SplitPaneFactory implements ProjectorNodeFactory<SplitPaneModel, SplitPane> {

    @Override
    public SplitPane create(final Projector projector, final SplitPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");
        final SplitPane splitPane = new SplitPane();
        new IndexedJavaFXListBinder<>(splitPane.getItems()).to(model.getItems(), info -> createDividerContent(projector, splitPane, info));
        bind(splitPane.orientationProperty()).to(model.orientationProperty());
        return splitPane;
    }

    private Node createDividerContent(final Projector projector, final SplitPane splitPane, final IndexedJavaFXListBinder.ConversionInfo<SplitPaneItemModel> info) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(splitPane, "splitPane");
        Assert.requireNonNull(info, "info");
        final SplitPaneItemModel model = info.getInput();
        Assert.requireNonNull(model, "model");
        final Node newNode = projector.createNode(model.getContent());
        splitPane.setDividerPosition(info.getIndex(), model.getDividerPosition());
        model.contentProperty().onChanged(evt -> splitPane.getItems().set(info.getIndex(), projector.createNode(evt.getNewValue())));
        return newNode;
    }

    @Override
    public Class<SplitPaneModel> getSupportedType() {
        return SplitPaneModel.class;
    }
}
