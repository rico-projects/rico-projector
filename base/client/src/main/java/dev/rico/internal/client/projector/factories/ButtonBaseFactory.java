package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.ButtonModel;
import javafx.scene.control.ButtonBase;

abstract class ButtonBaseFactory<T extends ButtonModel, S extends ButtonBase> implements ProjectorNodeFactory<T, S> {

    S createButtonBase(Projector projector, T model, S node) {
        configureButton(model, node);
        return node;
    }

    private void configureButton(T model, S node) {
        bind(node.textProperty()).to(model.captionProperty());
    }
}
