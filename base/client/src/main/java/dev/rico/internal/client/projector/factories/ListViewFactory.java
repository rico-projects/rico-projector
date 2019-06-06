package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.client.projector.mixed.ListCellSkin;
import dev.rico.internal.client.projector.uimanager.ClientUiHelper;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.listview.ListViewItemModel;
import dev.rico.internal.projector.ui.listview.ListViewModel;
import javafx.scene.control.ListView;
import to.remove.DocumentTemplateListCellSkin;
import to.remove.EditableListCell;

import java.lang.reflect.InvocationTargetException;

import static dev.rico.client.remoting.FXBinder.bind;

public class ListViewFactory implements ProjectorNodeFactory<ListViewModel, ListView<ListViewItemModel>> {

    @Override
    public ListView<ListViewItemModel> create(final Projector projector, final ListViewModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ListView<ListViewItemModel> listView = new ListView<>();
        bind(listView.getItems()).to(model.getItems());
        ClientUiHelper.bindWithSelectionModel(model.selectedProperty(), listView.getSelectionModel());
        if (model.getSelectedAction() != null) {
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> projector.getControllerProxy().invoke(model.getSelectedAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(listView, throwable)));
        }
        bind(listView.cellFactoryProperty()).to(model.rendererClassProperty(), className -> {
            if (className == null) return null;
            return view -> {
                try {
                    @SuppressWarnings("unchecked") ListCellSkin<ListViewItemModel> cellSkin = (ListCellSkin<ListViewItemModel>) Class.forName(className).getConstructor().newInstance();
                    cellSkin.setOwner(model);
                    cellSkin.setControllerProxy(projector.getControllerProxy());
                    return new EditableListCell<>(cellSkin);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new IllegalArgumentException(e);
                }
            };
        });
        model.rendererClassProperty().onChanged(evt -> listView.setCellFactory(view -> new EditableListCell<>(new DocumentTemplateListCellSkin())));
        return listView;
    }

    @Override
    public Class<ListViewModel> getSupportedType() {
        return ListViewModel.class;
    }
}
