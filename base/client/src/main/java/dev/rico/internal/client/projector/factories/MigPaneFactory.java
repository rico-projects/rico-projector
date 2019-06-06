package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import javafx.scene.Node;
import org.tbee.javafx.scene.layout.MigPane;
import to.remove.ui.migpane.MigPaneModel;

public class MigPaneFactory implements ProjectorNodeFactory<MigPaneModel, MigPane> {

    @Override
    public MigPane create(Projector projector, MigPaneModel model) {
        MigPane migPane = new MigPane(model.getLayoutConstraints(), model.getColumnConstraints(), model.getRowConstraints());
        model.getItems().forEach(migPaneItemModel -> {
            Node child = projector.createNode(migPaneItemModel.getItem());
            migPane.add(child, migPaneItemModel.getConstraints());
        });
        model.getItems().onChanged(evt -> {
            migPane.getChildren().clear();
            model.getItems().forEach(migPaneItemModel -> {
                Node child = projector.createNode(migPaneItemModel.getItem());
                migPane.add(child, migPaneItemModel.getConstraints());
            });
        });
        return migPane;
    }

    @Override
    public Class<MigPaneModel> getSupportedType() {
        return MigPaneModel.class;
    }
}
