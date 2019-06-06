package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneFactory implements ProjectorNodeFactory<GridPaneModel, GridPane> {

    @Override
    public GridPane create(final Projector projector, final GridPaneModel model) {
        GridPane gridPane = new GridPane();
        bind(gridPane.hgapProperty()).to(model.hGapProperty());
        bind(gridPane.vgapProperty()).to(model.vGapProperty());
        bind(gridPane.getChildren()).to(model.getItems(), content -> {
            Node child = createNode(content.getItem());
            GridPane.setRowIndex(child, content.getRow());
            GridPane.setColumnIndex(child, content.getCol());
            GridPane.setRowSpan(child, content.getRowSpan());
            GridPane.setColumnSpan(child, content.getColSpan());
            GridPane.setHalignment(child, content.gethAlignment());
            GridPane.setValignment(child, content.getvAlignment());
            GridPane.setHgrow(child, content.gethGrow());
            GridPane.setVgrow(child, content.getvGrow());
            return child;
        });
        return gridPane;
    }

    @Override
    public Class<GridPaneModel> getSupportedType() {
        return GridPaneModel.class;
    }
}
