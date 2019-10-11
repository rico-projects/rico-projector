package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.table.TableBooleanColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;

class CheckBoxTableColumn extends TableColumn<TableRowModel, Boolean> {
    CheckBoxTableColumn(Projector projector, TableBooleanColumnModel model) {
        setCellValueFactory(param -> {
            ManagedTableView.UserData userData = (ManagedTableView.UserData) param.getTableColumn().getUserData();
            Boolean property = (Boolean) param.getValue().getCells().get(userData.getOriginalIndex()).getValue();
            BooleanProperty booleanProperty = new SimpleBooleanProperty(property);
            booleanProperty.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                ManagedTableView tableView = (ManagedTableView) param.getTableView();
                tableView.onCommit(model.getCommitAction(), projector, param.getValue(), CheckBoxTableColumn.this, newValue);
            });
            return booleanProperty;
        });

        setCellFactory(CheckBoxTableCell.forTableColumn(param -> new SimpleBooleanProperty()));
        setCellFactory(column -> new CheckBoxTableCell<>());
    }
}
