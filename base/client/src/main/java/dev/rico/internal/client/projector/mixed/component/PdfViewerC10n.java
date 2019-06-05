package dev.rico.internal.client.projector.mixed.component;

import com.github.rodionmoiseev.c10n.annotations.De;
import dev.rico.internal.projector.ForRemoval;

@ForRemoval
public interface PdfViewerC10n {
    @De("Lupe")
    String magnify();
}
