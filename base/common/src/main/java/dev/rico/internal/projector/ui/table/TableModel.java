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
package dev.rico.internal.projector.ui.table;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableModel extends ItemModel {
    private ObservableList<TableColumnModel> columns;
    private ObservableList<TableColumnModel> sortOrder;
    private ObservableList<TableRowModel> rows;
    private Property<TableRowModel> selectedRow;
    private Property<Boolean> editable;
    private Property<ItemModel> placeholder;

    public ObservableList<TableColumnModel> getColumns() {
        return columns;
    }

    public ObservableList<TableColumnModel> getSortOrder() {
        return sortOrder;
    }

    public ObservableList<TableRowModel> getRows() {
        return rows;
    }

    public TableRowModel getSelectedRow() {
        return selectedRow.get();
    }

    public void setSelectedRow(TableRowModel selectedRow) {
        this.selectedRow.set(selectedRow);
    }

    public Property<TableRowModel> selectedRowProperty() {
        return selectedRow;
    }

    public ItemModel getPlaceholder() {
        return placeholder.get();
    }

    public Property<ItemModel> placeholderProperty() {
        return placeholder;
    }

    public void setPlaceholder(ItemModel placeholder) {
        this.placeholder.set(placeholder);
    }

    public Boolean getEditable() {
        return editable.get();
    }

    public void setEditable(Boolean editable) {
        this.editable.set(editable);
    }

    public Property<Boolean> editableProperty() {
        return editable;
    }
}
