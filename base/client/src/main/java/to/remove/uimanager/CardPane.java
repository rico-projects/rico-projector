package to.remove.uimanager;

import dev.rico.internal.projector.ForRemoval;
import to.remove.ui.cardpane.CardPaneModel;
import javafx.scene.layout.StackPane;

import static dev.rico.client.remoting.FXBinder.bind;

@ForRemoval
public
class CardPane extends StackPane {
   public CardPane(CardPaneModel model) {
      bind(alignmentProperty()).to(model.alignmentProperty());
   }
}
