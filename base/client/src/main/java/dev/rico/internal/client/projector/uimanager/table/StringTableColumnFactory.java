package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.internal.projector.ui.table.TableStringColumnModel;

public class StringTableColumnFactory implements TableColumnFactory<TableStringColumnModel, StringTableColumn> {
    @Override
    public Class<TableStringColumnModel> getSupportedType() {
        return TableStringColumnModel.class;
    }

    @Override
    public StringTableColumn create(Projector projector, TableStringColumnModel model) {
        return new StringTableColumn(projector, model);
    }
}
