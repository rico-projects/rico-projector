package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.cardpane.CardPaneModel;
import javafx.scene.layout.StackPane;

import static dev.rico.client.remoting.FXBinder.bind;

@ForRemoval
class CardPane extends StackPane {
   CardPane(CardPaneModel model) {
      bind(alignmentProperty()).to(model.alignmentProperty());
   }
}
