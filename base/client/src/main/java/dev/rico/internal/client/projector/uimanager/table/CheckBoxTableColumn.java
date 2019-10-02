package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableBooleanColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;

class CheckBoxTableColumn extends TableColumn<TableRowModel, Boolean> {
    CheckBoxTableColumn(Projector projector, TableBooleanColumnModel model) {
        super(model.getCaption());
        setCellFactory(column -> new CheckBoxTableCell<>());
    }
}
