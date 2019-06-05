package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.mixed.WeightUnit;
import dev.rico.internal.projector.ui.FuelFieldModel;
import dev.rico.remoting.ValueChangeEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@ForRemoval
public class FuelField extends AutoCompleteField<FuelInfo> {
    private final FuelFieldModel model;

    FuelField(FuelFieldModel model) {
        this.model = Objects.requireNonNull(model);

        model.weightUnitProperty().onChanged((ValueChangeEvent<? extends WeightUnit> evt) -> {
            WeightUnit newUnit = evt.getNewValue();
            if (newUnit != null) {
                FuelInfo fuelInfo = getFormatter().getValue();
                if (fuelInfo != null) {
                    try {
                        getFormatter().setValue(FuelInfo.from(fuelInfo.toString(), newUnit));
                    } catch (IllegalArgumentException e) {
                        getFormatter().setValue(null);
                    }
                }
            }
        });
    }

    @Override
    protected Collection<String> getDefaultSuggestions(ISuggestionRequest suggestionRequest) {
        return Arrays.stream(FuelInfo.SpecialValues.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
    }

    @Override
    protected Collection<String> getMatchingSuggestions(ISuggestionRequest suggestionRequest) {
        ArrayList<String> result = new ArrayList<>();
        String userText = suggestionRequest.getUserText();
        String formatted = "";
        try {
            formatted = FuelInfo.from(userText, model.getWeightUnit()).toString();
        } catch (IllegalArgumentException e) {
            userText = userText.toUpperCase();
            String finalUserText = userText;
            if (!finalUserText.isEmpty()) {
              result.addAll(Arrays.stream(FuelInfo.SpecialValues.values())
                      .map(Enum::toString)
                      .filter(specialValue -> specialValue.contains(finalUserText))
                              .collect(Collectors.toList()));
            }
        }
        if (!formatted.isEmpty()) {
            result.add(formatted);
        }
        return result;
    }

    @Override
    protected FuelInfo convertFromString(String string) {
        return FuelInfo.from(string, model.getWeightUnit());
    }
}
