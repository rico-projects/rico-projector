package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableDoubleColumnModel;

public class DoubleTableColumnFactory implements TableColumnFactory<TableDoubleColumnModel, DoubleTableColumn> {
    @Override
    public Class<TableDoubleColumnModel> getSupportedType() {
        return TableDoubleColumnModel.class;
    }

    @Override
    public DoubleTableColumn create(Projector projector, TableDoubleColumnModel model) {
        return new DoubleTableColumn(projector, model);
    }
}
