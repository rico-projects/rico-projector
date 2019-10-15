package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableDoubleColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

class DoubleTableColumn extends TableColumn<TableRowModel, Double> {
    DoubleTableColumn(Projector projector, TableDoubleColumnModel model) {
        setCellFactory(column -> createDoubleCell(model));
    }

    private TableCell<TableRowModel, Double> createDoubleCell(TableDoubleColumnModel model) {
        TextFieldTableCell<TableRowModel, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());
        cell.getStyleClass().add("double-table-cell");
        return cell;
    }
}
