package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableInstantColumnModel;

public class InstantTableColumnFactory implements TableColumnFactory<TableInstantColumnModel, InstantTableColumn> {
    @Override
    public Class<TableInstantColumnModel> getSupportedType() {
        return TableInstantColumnModel.class;
    }

    @Override
    public InstantTableColumn create(Projector projector, TableInstantColumnModel model) {
        return new InstantTableColumn(projector, model);
    }
}
