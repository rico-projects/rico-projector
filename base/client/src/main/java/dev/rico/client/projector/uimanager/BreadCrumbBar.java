package dev.rico.client.projector.uimanager;


import dev.rico.client.projector.uimanager.mixed.BreadCrumbBarSkin;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.client.remoting.Param;
import dev.rico.common.projector.ui.ManagedUiModel;
import dev.rico.common.projector.ui.breadcrumbbar.BreadCrumbBarModel;
import dev.rico.common.projector.ui.breadcrumbbar.BreadCrumbItemModel;
import dev.rico.remoting.ObservableList;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

class BreadCrumbBar extends dev.rico.client.projector.uimanager.mixed.BreadCrumbBar<BreadCrumbItemModel> {
    BreadCrumbBar(BreadCrumbBarModel model, ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        setAutoNavigationEnabled(false);
        setMinWidth(10.0);

        setCrumbFactory(crumb -> {
            BreadCrumbBarSkin.BreadCrumbButton<BreadCrumbItemModel> button = new BreadCrumbBarSkin.BreadCrumbButton<>("", crumb.getValue());
            if (crumb.getValue() == null) {
                FXBinder.bind(button.textProperty()).to(model.captionProperty());
            } else {
                button.setText(crumb.getValue().getCaption());
                FXBinder.bind(button.textProperty()).to(crumb.getValue().captionProperty());
            }
            button.setSelected(model.getSelected() == crumb.getValue());
            return button;
        });

        setOnCrumbAction(event -> {
            BreadCrumbItemModel value = event.getSelectedCrumb().getValue();
            model.setSelected(value);
            controllerProxy.invoke("onBreadCrumbSelected",
                    new Param("model", model), new Param("selection", value))
                    .exceptionally(throwable -> UnexpectedErrorDialog.showError(this, throwable));
        });

        setSelectedCrumb(createCrumbTree(model.getItems()));
        FXWrapper.wrapList(model.getItems()).addListener((InvalidationListener) observable -> {
            // Dies wurde auskommentiert, weil sonst nach dem Hinzufügen ("+")
            // eines Fluges der Crumb "Gesamter Umlauf" ausgewählt ist statt des neuen Fluges!
//         model.setSelected(null);
            setSelectedCrumb(createCrumbTree(model.getItems()));
        });

        selectButton(model.getSelected());
        model.selectedProperty().onChanged(valueChangeEvent -> {
            selectButton(valueChangeEvent.getNewValue());
        });
    }

    private void selectButton(BreadCrumbItemModel newValue) {
        for (Node node : getChildrenUnmodifiable()) {
            if (node instanceof BreadCrumbBarSkin.BreadCrumbButton) {
                @SuppressWarnings("unchecked")
                BreadCrumbBarSkin.BreadCrumbButton<BreadCrumbItemModel> button = (BreadCrumbBarSkin.BreadCrumbButton<BreadCrumbItemModel>) node;
                BreadCrumbItemModel value = button.getValue();
                if (value == newValue) {
                    button.setSelected(true);
                    break;
                }
            }
        }
    }

    private TreeItem<BreadCrumbItemModel> createCrumbTree(ObservableList<BreadCrumbItemModel> items) {
        Object[] itemArray = new Object[items.size() + 1];
        int pos = 0;
        for (BreadCrumbItemModel item : items) {
            itemArray[++pos] = item;
        }
        return (TreeItem) BreadCrumbBar.buildTreeModel(itemArray);
    }
}
