package dev.rico.client.projector.mixed;

import dev.rico.common.projector.mixed.RemotingEvent;

public interface DolphinEventHandler {
    void onEvent(RemotingEvent event);
}
