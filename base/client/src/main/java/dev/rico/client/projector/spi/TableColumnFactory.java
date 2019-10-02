package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableColumnModel;

public interface TableColumnFactory<T extends TableColumnModel, C> extends TypeBasedProvider<T> {
    C create(Projector projector, T model);
}
