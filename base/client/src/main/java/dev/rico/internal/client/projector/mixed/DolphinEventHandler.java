package dev.rico.internal.client.projector.mixed;

import dev.rico.internal.projector.mixed.RemotingEvent;

public interface DolphinEventHandler {
    void onEvent(RemotingEvent event);
}
