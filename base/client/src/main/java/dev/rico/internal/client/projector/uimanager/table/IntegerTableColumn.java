package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableIntegerColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

class IntegerTableColumn extends TableColumn<TableRowModel, Integer> {
    IntegerTableColumn(Projector projector, TableIntegerColumnModel model) {
        super(model.getCaption());
        setCellFactory(column -> createIntegerCell(model));
        setOnEditCommit(event -> System.out.println("Neuer Wert: " + event.getNewValue()));
    }

    private TableCell<TableRowModel, Integer> createIntegerCell(TableIntegerColumnModel model) {
        TextFieldTableCell<TableRowModel, Integer> cell = new TextFieldTableCell<>((new IntegerStringConverter()));
        cell.setAlignment(Pos.CENTER_RIGHT);
        return cell;
    }
}
