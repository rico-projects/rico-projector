package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.box.HBoxItemModel;
import dev.rico.internal.projector.ui.box.HBoxModel;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import static dev.rico.client.remoting.FXBinder.bind;

public class HBoxFactory implements ProjectorNodeFactory<HBoxModel, HBox> {

    @Override
    public HBox create(final Projector projector, final HBoxModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final HBox hBox = new HBox();
        bind(hBox.spacingProperty()).to(model.spacingProperty(), value -> getValue(value, 0));
        bind(hBox.alignmentProperty()).to(model.alignmentProperty());
        bind(hBox.getChildren()).to(model.getItems(), content -> setHgrow(projector, content));
        return hBox;
    }

    private Node setHgrow(final Projector projector, final HBoxItemModel content) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(content, "content");

        final Node child = projector.createNode(content.getItem());
        HBox.setHgrow(child, content.gethGrow());
        return child;
    }

    @Override
    public Class<HBoxModel> getSupportedType() {
        return HBoxModel.class;
    }
}
