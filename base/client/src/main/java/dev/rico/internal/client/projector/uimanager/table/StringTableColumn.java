package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.internal.projector.ui.table.TableStringColumnModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;

class StringTableColumn extends TableColumn<TableRowModel,String>{
    StringTableColumn(Projector projector, TableStringColumnModel model) {
        super(model.getCaption());
        setCellFactory(TextFieldTableCell.forTableColumn());
    }
}
