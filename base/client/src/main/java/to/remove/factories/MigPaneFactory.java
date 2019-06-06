package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.remoting.ObservableList;
import javafx.scene.Node;
import org.tbee.javafx.scene.layout.MigPane;
import to.remove.ui.migpane.MigPaneItemModel;
import to.remove.ui.migpane.MigPaneModel;

public class MigPaneFactory implements ProjectorNodeFactory<MigPaneModel, MigPane> {

    @Override
    public MigPane create(final Projector projector, final MigPaneModel model) {
        Assert.requireNonNull(model, "model");

        final MigPane migPane = new MigPane(model.getLayoutConstraints(), model.getColumnConstraints(), model.getRowConstraints());
        final ObservableList<MigPaneItemModel> items = model.getItems();
        Assert.requireNonNull(items, "items");

        items.forEach(migPaneItemModel -> {
            final Node child = projector.createNode(migPaneItemModel.getItem());
            migPane.add(child, migPaneItemModel.getConstraints());
        });
        items.onChanged(evt -> {
            migPane.getChildren().clear();
            items.forEach(migPaneItemModel -> {
                final Node child = projector.createNode(migPaneItemModel.getItem());
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
