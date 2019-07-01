package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.Optional;

public interface DialogConfiguration {

    default Optional<Node> configureDialog(final Projector projector, final Alert alert, final DialogModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(alert, "alert");
        Assert.requireNonNull(model, "model");
        final Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(projector.getModelToNodeMap()::get);
        final Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(alert::initOwner);
        alert.setTitle(model.getTitle());
        return nodeOptional;
    }

    //TODO: Method not referenced. Can we remove it?
    default Optional<Window> findWindowOptional(final Projector projector, final DialogModel newDialog) {
        final Optional<Node> nodeOptional = Optional.ofNullable(newDialog.getOwner()).map(projector.getModelToNodeMap()::get);
        return nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
    }
}
