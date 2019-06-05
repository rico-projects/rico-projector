package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.mixed.PaxInfo;
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import java.util.ArrayList;
import java.util.Collection;

@ForRemoval
public class PaxCodeField extends AutoCompleteField<PaxInfo> {

    @Override
    protected Collection<String> getDefaultSuggestions(ISuggestionRequest suggestionRequest) {
        return new ArrayList<>();
    }

    @Override
    protected Collection<String> getMatchingSuggestions(ISuggestionRequest suggestionRequest) {
        ArrayList<String> result = new ArrayList<>();
        result.add(PaxInfo.from(suggestionRequest.getUserText()).toString());
        return result;
    }

    @Override
    protected PaxInfo convertFromString(String string) {
        return PaxInfo.from(string);
    }
}
