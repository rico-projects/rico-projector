package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.BorderPaneModel;
import dev.rico.common.projector.ui.ImageViewModel;
import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.LabelModel;
import dev.rico.common.projector.ui.box.HBoxModel;
import dev.rico.common.projector.ui.box.VBoxModel;
import javafx.geometry.Pos;

class JobFinishedPane extends UiPane {
    public JobFinishedPane(ManagedUiController managedRootUiController) {
        super(managedRootUiController);
        getModel().setRoot(getPane());
    }

    @Override
    public ItemModel buildUi() {
        BorderPaneModel borderPane = ui().borderPane();
        VBoxModel centerBox = ui().vBox();
        centerBox.setAlignment(Pos.CENTER);
        HBoxModel finishImageAndLinksBox = ui().hBox();
        finishImageAndLinksBox.setAlignment(Pos.CENTER);
        finishImageAndLinksBox.setSpacing(20);
        ImageViewModel imageView = ui().imageView("classpath:/image/success.png");
        imageView.setFitWidth(120.0);
        finishImageAndLinksBox.add(ui().hBoxItem(imageView));
        VBoxModel box = ui().vBox();
        box.setAlignment(Pos.CENTER_LEFT);
        finishImageAndLinksBox.add(ui().hBoxItem(box));
//        LabelModel itemModel = ui().label("Fertig. Was möchtest Du als nächstes machen?");
        LabelModel itemModel = ui().label("Fertig. ");
        itemModel.setStyle("-fx-font-size: 24");
        box.add(ui().vBoxItem(itemModel));
//         box.add(ui().vBoxItem(ui().hyperlink("Zurück zum Flugabschnitt", Constants.BACK_TO_FLIGHT)));
        centerBox.getItems().add(ui().vBoxItem(finishImageAndLinksBox));
        borderPane.setCenter(centerBox);
        return borderPane;
    }
}
