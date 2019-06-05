package dev.rico.internal.client.projector.mixed;

import com.github.rodionmoiseev.c10n.annotations.De;
import dev.rico.internal.projector.ForRemoval;

@ForRemoval
public interface EditableListCellC10n {
    @De("Den Eintrag bearbeiten")
    String editEntry();
    @De("Änderungen übernehmen")
    String apply();
    @De("Änderungen verwerfen")
    String cancel();
}
