package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import to.remove.ui.cardpane.CardPaneModel;
import to.remove.uimanager.CardPane;

public class CardPaneFactory implements ProjectorNodeFactory<CardPaneModel, CardPane> {

    @Override
    public CardPane create(final Projector projector, final CardPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        return new CardPane(model);
    }

    @Override
    public Class<CardPaneModel> getSupportedType() {
        return CardPaneModel.class;
    }
}
