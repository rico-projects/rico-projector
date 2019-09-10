/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 The original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.factories.converters.ChoiceBoxItemConverter;
import dev.rico.internal.client.projector.factories.converters.PlainStringConverter;
import dev.rico.internal.client.projector.mixed.FormatterFactory;
import dev.rico.internal.client.projector.uimanager.ClientUiHelper;
import dev.rico.internal.client.projector.uimanager.IndexedJavaFXListBinder;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.table.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.time.Instant;

import static dev.rico.client.remoting.FXBinder.bind;

public class TableFactory implements ProjectorNodeFactory<TableModel, TableView> {

    @Override
    public TableView create(final Projector projector, final TableModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final TableView<TableRowModel> table = new TableView<>();
        bind(table.editableProperty()).to(model.editableProperty(), value -> getValue(value, false));
        new IndexedJavaFXListBinder<>(table.getColumns()).bidirectionalTo(model.getColumns(), conversionInfo -> createTableColumn(projector, table, conversionInfo), conversionInfo -> (TableColumnModel) conversionInfo.getInput().getUserData());

        // bidirektional, damit Tabellensortierung funktioniert
        bind(table.getItems()).bidirectionalTo(model.getRows());
        ClientUiHelper.bindWithSelectionModel(model.selectedRowProperty(), table.getSelectionModel());

        FXBinder.bind(table.placeholderProperty()).to(model.placeholderProperty(),
                itemModel -> itemModel == null ? new Label("Nichts gefunden.") : projector.createNode(itemModel));

        return table;
    }

    @Override
    public Class<TableModel> getSupportedType() {
        return TableModel.class;
    }

    private TableColumn<TableRowModel, ?> createTableColumn(final Projector projector, final TableView<TableRowModel> table, final IndexedJavaFXListBinder.ConversionInfo<TableColumnModel> conversionInfo) {
        final TableColumn<TableRowModel, Object> tableColumn = new TableColumn<>(conversionInfo.getInput().getCaption());

        new IndexedJavaFXListBinder<>(tableColumn.getColumns()).bidirectionalTo(conversionInfo.getInput().getChildren(), subConversionInfo -> createTableColumn(projector, table, subConversionInfo), subConversionInfo -> (TableColumnModel) subConversionInfo.getInput().getUserData());

        table.setUserData(conversionInfo.getInput());
        bind(tableColumn.textProperty()).to(conversionInfo.getInput().captionProperty());
        bind(tableColumn.prefWidthProperty()).to(conversionInfo.getInput().prefWidthProperty(), value -> getValue(value, 80));
        bind(tableColumn.editableProperty()).to(conversionInfo.getInput().editableProperty(), value -> getValue(value, true));
        bind(tableColumn.visibleProperty()).to(conversionInfo.getInput().visibleProperty(), value -> getValue(value, true));

        tableColumn.setCellValueFactory(param -> FXWrapper.wrapObjectProperty(
                param.getValue().getCells().get(conversionInfo.getIndex()).valueProperty()));
        if (conversionInfo.getInput() instanceof TableIntegerColumnModel) {
            ((TableColumn) tableColumn).setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            tableColumn.setOnEditCommit(event -> onCommit("onTableIntegerCommit", projector, table, event, conversionInfo));
        } else if (conversionInfo.getInput() instanceof TableDoubleColumnModel) {
            bind(tableColumn.cellFactoryProperty()).to(conversionInfo.getInput().cellFactoryClassProperty(), className -> {
                if (className == null) return null;
                try {
                    return (Callback) Class.forName(className).getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            });
            tableColumn.setOnEditCommit(event -> onCommit("onTableDoubleCommit", projector, table, event, conversionInfo));
        } else if (conversionInfo.getInput() instanceof TableInstantColumnModel) {
            final TableInstantColumnModel columnModel = (TableInstantColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(column -> new TableCell<TableRowModel, Object>() {
                protected void updateItem(final Object item, final boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else if (columnModel.getFormatString() != null) {
                        setText(FormatterFactory.customFormat(
                                columnModel.getFormatString()).format((Instant) item));
                    } else {
                        setText(FormatterFactory.dateFormatter().format((Instant) item));
                    }
                }
            });
        } else if (conversionInfo.getInput() instanceof TableStringColumnModel) {
            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PlainStringConverter()));
            tableColumn.setOnEditCommit(
                    event -> onCommit("onTableStringCommit", projector, table, event, conversionInfo));
        } else if (conversionInfo.getInput() instanceof TableChoiceBoxColumnModel) {
            final TableChoiceBoxColumnModel choiceBoxColumnModel = (TableChoiceBoxColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(ChoiceBoxTableCell
                    .forTableColumn(new ChoiceBoxItemConverter(choiceBoxColumnModel.getItems()), (javafx.collections.ObservableList) FXWrapper.wrapList(choiceBoxColumnModel.getItems())));
            tableColumn.setOnEditCommit(event -> onCommit("onTableChoiceBoxCommit", projector, table, event, conversionInfo));
        } else if (conversionInfo.getInput() instanceof TableCheckBoxColumnModel) {
            tableColumn.setCellValueFactory(param -> {
                final TableRowModel row = param.getValue();
                final BooleanProperty property = new SimpleBooleanProperty((Boolean) row.getCells().get(conversionInfo.getIndex()).getValue());
                property.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> projector.getControllerProxy().invoke("onTableCheckBoxCommit",
                        createRow(param.getValue()),
                        createColumn(conversionInfo.getInput()),
                        createNewValue(newValue)).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable)));
                return (ObservableValue) property;
            });
            tableColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        }
        return tableColumn;
    }

    private void onCommit(final String name, final Projector projector, final TableView<TableRowModel> table, final TableColumn.CellEditEvent<TableRowModel, Object> event, final IndexedJavaFXListBinder.ConversionInfo<TableColumnModel> conversionInfo) {
        projector.getControllerProxy().invoke(name,
                createRow(event.getRowValue()),
                createColumn(conversionInfo.getInput()),
                createNewValue(event.getNewValue())).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable));

    }

    private Param createRow(final IdentifiableModel model) {
        return new Param("row", model.getReference());
    }

    private Param createColumn(final IdentifiableModel model) {
        return new Param("column", model.getReference());
    }

    private Param createNewValue(final Object value) {
        return new Param("newValue", value);
    }

}
