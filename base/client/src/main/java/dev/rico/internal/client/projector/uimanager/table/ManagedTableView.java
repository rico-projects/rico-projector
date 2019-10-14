package dev.rico.internal.client.projector.uimanager.table;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.DefaultValueAccess;
import dev.rico.client.projector.spi.ServiceProviderAccess;
import dev.rico.client.projector.spi.TableColumnFactory;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.uimanager.ClientUiHelper;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.table.TableCellModel;
import dev.rico.internal.projector.ui.table.TableColumnModel;
import dev.rico.internal.projector.ui.table.TableModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.remoting.ObservableList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static dev.rico.client.remoting.FXBinder.bind;

public class ManagedTableView extends TableView<TableRowModel> implements DefaultValueAccess, ServiceProviderAccess {
    private final Projector projector;
    private final TableModel model;
    private final Map<Class<?>, List<TableColumnFactory>> tableColumnFactories;

    @SuppressWarnings("unchecked")
    public ManagedTableView(Projector projector, TableModel model) {
        tableColumnFactories = loadServiceProviders(TableColumnFactory.class);
        this.projector = Objects.requireNonNull(projector);
        this.model = Objects.requireNonNull(model);
        javafx.collections.ObservableList<TableRowModel> data = FXCollections.observableArrayList();
        SortedList<TableRowModel> sortedList = new SortedList<>(data);
        sortedList.comparatorProperty().bind(comparatorProperty());
        setItems(sortedList);
        bind(editableProperty()).to(model.editableProperty(), value -> getValue(value, false));
        bind(getColumns()).bidirectionalTo(model.getColumns(), this::createColumn, this::getModel);
        bind(getSortOrder()).bidirectionalTo(model.getSortOrder(), this::findMatchingColumn, this::getMatchingModel);
        bind(data).bidirectionalTo(model.getRows());
        bind(placeholderProperty()).to(model.placeholderProperty(), this::createPlaceholder);
        ClientUiHelper.bindWithSelectionModel(model.selectedRowProperty(), getSelectionModel());
    }

    private TableColumnModel getMatchingModel(TableColumn<TableRowModel, ?> c) {
        return ((UserData) c.getUserData()).model;
    }

    private TableColumn<TableRowModel, ?> findMatchingColumn(TableColumnModel tableColumnModel) {
        return getColumns().stream().filter(model -> getMatchingModel(model).equals(tableColumnModel)).findAny().orElseThrow(RuntimeException::new);
    }

    private Node createPlaceholder(ItemModel itemModel) {
        return itemModel == null ? new Label("Nichts gefunden.") : projector.createNode(itemModel);
    }

    private TableColumnModel getModel(TableColumn<TableRowModel, ?> treeTableColumn) {
        return getMatchingModel(treeTableColumn);
    }

    static public class UserData {
        private final TableColumnModel model;
        private final int originalIndex;

        private UserData(TableColumnModel model, int originalIndex) {
            this.model = model;
            this.originalIndex = originalIndex;
        }

        public TableColumnModel getModel() {
            return model;
        }

        public int getOriginalIndex() {
            return originalIndex;
        }
    }

    @SuppressWarnings("unchecked")
    private TableColumn<TableRowModel, ?> createColumn(TableColumnModel columnModel) {
        List<TableColumnFactory> factory = tableColumnFactories.get(columnModel.getClass());
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for " + columnModel.getClass());
        }
        TableColumn<TableRowModel, ?> column = (TableColumn) factory.iterator().next().create(projector, columnModel);
        column.setUserData(new UserData(columnModel, this.model.getColumns().indexOf(columnModel)));
        if (column.getCellValueFactory() == null) {
            column.setCellValueFactory(param -> {
                UserData userData = (UserData) param.getTableColumn().getUserData();
                TableRowModel managedRow = param.getValue();
                ObservableList<TableCellModel> values = managedRow.getCells();
                TableCellModel cell = values.get(userData.originalIndex);
                SimpleObjectProperty simpleObjectProperty = new SimpleObjectProperty(cell.getValue());
                cell.valueProperty().onChanged(evt -> {
                    simpleObjectProperty.setValue(evt.getNewValue());
                });
                return simpleObjectProperty;
            });
        }
        column.setOnEditCommit(event -> onCommit(columnModel.getCommitAction(), projector, event));
        bind(column.textProperty()).to(columnModel.captionProperty(), defaultValue(() -> ""));
        bind(column.editableProperty()).to(columnModel.editableProperty(), defaultValue(column::isEditable));
        bind(column.editableProperty()).to(columnModel.editableProperty(), defaultValue(column::isEditable));
        bind(column.visibleProperty()).bidirectionalTo(columnModel.visibleProperty(), defaultValueBidirectional(column::isVisible));
        bind(column.sortTypeProperty()).bidirectionalTo(columnModel.sortTypeProperty(), defaultValueBidirectional(column::getSortType));
        bind(column.prefWidthProperty()).bidirectionalToNumeric(columnModel.prefWidthProperty(), defaultValueBidirectional(column::getPrefWidth));
        return column;
    }

    private void onCommit(final String name, final Projector projector, final TableColumn.CellEditEvent<TableRowModel, ?> event) {
        onCommit(name, projector, event.getRowValue(), event.getTableColumn(), event.getNewValue());
    }

    void onCommit(String name, Projector projector, TableRowModel rowValue, TableColumn<TableRowModel, ?> tableColumn, Object newValue) {
        projector.getControllerProxy().invoke(name,
                createRowParam(rowValue),
                createColumnParam(getMatchingModel(tableColumn)),
                createNewValueParam(newValue)).exceptionally(throwable -> UnexpectedErrorDialog.showError(this, throwable));
    }

    private Param createRowParam(final IdentifiableModel model) {
        return new Param("row", model.getReference());
    }

    private Param createColumnParam(final IdentifiableModel model) {
        return new Param("column", model.getReference());
    }

    private Param createNewValueParam(final Object value) {
        Object realValue = value;
        if (value instanceof IdentifiableModel) {
            realValue = ((IdentifiableModel) value).getReference();
        }
        return new Param("newValue", realValue);
    }
}
