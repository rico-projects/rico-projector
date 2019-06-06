package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.box.VBoxModel;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import static dev.rico.client.remoting.FXBinder.bind;

public class VBoxFactory implements ProjectorNodeFactory<VBoxModel, VBox> {

    @Override
    public VBox create(final Projector projector, final VBoxModel model) {
        VBox vBox = new VBox();
        bind(vBox.spacingProperty()).to(model.spacingProperty(), value -> getValue(value, 0));
        bind(vBox.alignmentProperty()).to(model.alignmentProperty());
        bind(vBox.getChildren()).to(model.getItems(), content -> {
            Node child = projector.createNode(content.getItem());
            VBox.setVgrow(child, content.getvGrow());
            return child;
        });
        return vBox;
    }

    @Override
    public Class<VBoxModel> getSupportedType() {
        return VBoxModel.class;
    }
}
