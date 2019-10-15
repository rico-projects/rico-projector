package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.internal.projector.ui.table.TableStringColumnModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

class StringTableColumn extends TableColumn<TableRowModel, String> {
    StringTableColumn(Projector projector, TableStringColumnModel model) {
        setCellFactory((param) -> {
            TextFieldTableCell<TableRowModel, String> cell = new TextFieldTableCell<>(new DefaultStringConverter());
            cell.getStyleClass().add("string-table-cell");
            return cell;
        });
    }
}
