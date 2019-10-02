package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.internal.client.projector.mixed.FormatterFactory;
import dev.rico.internal.projector.ui.table.TableInstantColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.time.Instant;

class InstantTableColumn extends TableColumn<TableRowModel, Instant> {
    InstantTableColumn(Projector projector, TableInstantColumnModel model) {
        super(model.getCaption());
        setCellFactory(column -> createInstantCell(model));
        setOnEditCommit(event -> System.out.println("Neuer Wert: " + event.getNewValue()));
    }

    private TableCell<TableRowModel, Instant> createInstantCell(TableInstantColumnModel model) {
        TableCell<TableRowModel, Instant> tableCell = new InstantTableCell(model);
        tableCell.setAlignment(Pos.CENTER_RIGHT);
        return tableCell;
    }

    private static class InstantTableCell extends TableCell<TableRowModel, Instant> {
        private final TableInstantColumnModel model;

        InstantTableCell(TableInstantColumnModel model) {
            this.model = model;
        }

        protected void updateItem(final Instant item, final boolean empty) {
            super.updateItem(item, empty);
            String text;
            if (item == null || empty) {
                text = null;
            } else if (model.getFormatString() != null) {
                text = FormatterFactory.customFormat(model.getFormatString()).format(item);
            } else {
                text = FormatterFactory.dateFormatter().format(item);
            }
            setText(text);
        }
    }
}
