package dev.rico.client.projector;

import dev.rico.internal.projector.ui.IdentifiableModel;

public interface PostProcessor {
    void postProcess(String id, IdentifiableModel model, Object node);
}
