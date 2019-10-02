package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableBooleanColumnModel;

public class CheckBoxTableColumnFactory implements TableColumnFactory<TableBooleanColumnModel, CheckBoxTableColumn> {
    @Override
    public Class<TableBooleanColumnModel> getSupportedType() {
        return TableBooleanColumnModel.class;
    }

    @Override
    public CheckBoxTableColumn create(Projector projector, TableBooleanColumnModel model) {
        return new CheckBoxTableColumn(projector, model);
    }
}
