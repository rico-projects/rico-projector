package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableIntegerColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

class IntegerTableColumn extends TableColumn<TableRowModel, Integer> {
    IntegerTableColumn(Projector projector, TableIntegerColumnModel model) {
        setCellFactory(column -> createIntegerCell(model));
    }

    private TableCell<TableRowModel, Integer> createIntegerCell(TableIntegerColumnModel model) {
        TextFieldTableCell<TableRowModel, Integer> cell = new TextFieldTableCell<>((new IntegerStringConverter()));
        cell.getStyleClass().add("integer-table-cell");
        return cell;
    }
}
