package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.internal.client.projector.factories.converters.ChoiceBoxItemConverter;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.table.TableChoiceBoxColumnModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.remoting.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ChoiceBoxTableCell;

class ChoiceBoxTableColumn extends TableColumn<TableRowModel, ChoiceBoxItemModel> {
    ChoiceBoxTableColumn(Projector projector, TableChoiceBoxColumnModel model) {
        ObservableList<ChoiceBoxItemModel> items = model.getItems();
        setCellFactory(ChoiceBoxTableCell.forTableColumn(new ChoiceBoxItemConverter(items), FXWrapper.wrapList(items)));
    }
}
