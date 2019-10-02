package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableChoiceBoxColumnModel;

public class ChoiceBoxTableColumnFactory implements TableColumnFactory<TableChoiceBoxColumnModel, ChoiceBoxTableColumn> {
    @Override
    public Class<TableChoiceBoxColumnModel> getSupportedType() {
        return TableChoiceBoxColumnModel.class;
    }

    @Override
    public ChoiceBoxTableColumn create(Projector projector, TableChoiceBoxColumnModel model) {
        return new ChoiceBoxTableColumn(projector, model);
    }
}
