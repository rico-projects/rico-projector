package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.controlsfx.control.HiddenSidesPane;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ValueChangeEvent;
import javafx.scene.Node;
import to.remove.ui.HiddenSidesPaneModel;

public class HiddenSidesPaneFactory implements ProjectorNodeFactory<HiddenSidesPaneModel, HiddenSidesPane> {

    @Override
    public HiddenSidesPane create(final Projector projector, final HiddenSidesPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final HiddenSidesPane pane = new HiddenSidesPane();
        pane.setTop(convert(projector, model::getTop));
        pane.setLeft(convert(projector, model::getLeft));
        pane.setRight(convert(projector, model::getRight));
        pane.setBottom(convert(projector, model::getBottom));
        pane.setContent(convert(projector, model::getContent));
        model.topProperty().onChanged(evt -> handleEvent(projector, evt, pane::setTop));
        model.leftProperty().onChanged(evt -> handleEvent(projector, evt, pane::setLeft));
        model.rightProperty().onChanged(evt -> handleEvent(projector, evt, pane::setRight));
        model.bottomProperty().onChanged(evt -> handleEvent(projector, evt, pane::setBottom));
        //        itemModel.contentProperty().onChanged(evt -> pane.setContent(convert.call(evt.getNewValue())));
        bind(pane.contentProperty()).to(model.contentProperty(), projector::createNode);
        bind(pane.pinnedSideProperty()).to(model.pinnedSideProperty());
        return pane;
    }

    @Override
    public Class<HiddenSidesPaneModel> getSupportedType() {
        return HiddenSidesPaneModel.class;
    }

    private void handleEvent(final Projector projector, final ValueChangeEvent<? extends ItemModel> event, final Consumer<Node> con) {
        con.accept(convert(projector, event::getNewValue));
    }

    private Node convert(final Projector projector, final Supplier<ItemModel> sup) {
        if (sup.get() == null) {
            return null;
        }
        return projector.createNode(sup.get());
    }
}
