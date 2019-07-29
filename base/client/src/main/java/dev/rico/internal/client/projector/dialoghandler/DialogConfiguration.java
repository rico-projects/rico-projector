package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.Optional;

public interface DialogConfiguration {

    default Optional<Node> configureDialog(Projector projector, Alert alert, DialogModel model) {
        Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(projector.getModelToNodeMap()::get);
        Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(alert::initOwner);
        alert.setTitle(model.getTitle());
        return nodeOptional;
    }
}
