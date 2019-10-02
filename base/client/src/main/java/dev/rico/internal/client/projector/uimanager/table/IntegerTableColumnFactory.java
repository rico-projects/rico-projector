package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableIntegerColumnModel;

public class IntegerTableColumnFactory implements TableColumnFactory<TableIntegerColumnModel, IntegerTableColumn> {
    @Override
    public Class<TableIntegerColumnModel> getSupportedType() {
        return TableIntegerColumnModel.class;
    }

    @Override
    public IntegerTableColumn create(Projector projector, TableIntegerColumnModel model) {
        return new IntegerTableColumn(projector, model);
    }
}
