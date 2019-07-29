package dev.rico.internal.client.projector.factories.converters;

import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.remoting.ObservableList;
import javafx.util.StringConverter;

public class ChoiceBoxItemConverter extends StringConverter<Object> {

    private final ObservableList<ChoiceBoxItemModel> items;

    public ChoiceBoxItemConverter(final ObservableList<ChoiceBoxItemModel> items) {
        this.items = items;
    }

    @Override
    public String toString(final Object object) {
        if (object == null)
            return null;
        if (object instanceof ChoiceBoxItemModel) {
            return ((ChoiceBoxItemModel) object).getCaption();
        }
        throw new IllegalArgumentException("No " + ChoiceBoxItemModel.class.getSimpleName());
    }

    @Override
    public Object fromString(final String caption) {
        return items.stream().filter(choiceBoxItemModel -> choiceBoxItemModel.getCaption().equals(caption)).findFirst()
                .orElse(null);
    }
}