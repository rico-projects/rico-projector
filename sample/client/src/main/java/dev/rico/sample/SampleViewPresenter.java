package dev.rico.sample;

import dev.rico.internal.client.projector.uimanager.ManagedUiViewController;
import dev.rico.internal.projector.ui.ManagedUiModel;

class SampleViewPresenter extends ManagedUiViewController<ManagedUiModel> {

    public SampleViewPresenter(String controllerName) {
        super(controllerName);
    }
}