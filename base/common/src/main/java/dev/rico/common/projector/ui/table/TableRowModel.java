package dev.rico.common.projector.ui.table;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableRowModel extends IdentifiableModel {
    private ObservableList<TableCellModel> cells;

    public ObservableList<TableCellModel> getCells() {
        return cells;
    }
}
