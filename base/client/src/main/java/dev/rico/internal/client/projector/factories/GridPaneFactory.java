package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneFactory implements ProjectorNodeFactory<GridPaneModel, GridPane> {

    @Override
    public GridPane create(final Projector projector, final GridPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final GridPane gridPane = new GridPane();
        bind(gridPane.hgapProperty()).to(model.hGapProperty());
        bind(gridPane.vgapProperty()).to(model.vGapProperty());
        bind(gridPane.getChildren()).to(model.getItems(), content -> bindChildNode(projector, content));
        return gridPane;
    }

    @Override
    public Class<GridPaneModel> getSupportedType() {
        return GridPaneModel.class;
    }

    private Node bindChildNode(final Projector projector, final GridPaneItemModel model) {
        final Node child = projector.createNode(model.getItem());
        GridPane.setRowIndex(child, model.getRow());
        GridPane.setColumnIndex(child, model.getCol());
        GridPane.setRowSpan(child, model.getRowSpan());
        GridPane.setColumnSpan(child, model.getColSpan());
        GridPane.setHalignment(child, model.gethAlignment());
        GridPane.setValignment(child, model.getvAlignment());
        GridPane.setHgrow(child, model.gethGrow());
        GridPane.setVgrow(child, model.getvGrow());
        return child;
    }
}
