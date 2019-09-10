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

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@SuppressWarnings("ClassWithoutLogger")
@RemotingBean
public class TableColumnModel extends IdentifiableModel {
    private Property<String> cellFactoryClass;
    private ObservableList<TableColumnModel> children;
    private Property<String> caption;
    private Property<Boolean> editable;
    private Property<Boolean> visible;
    private Property<Double> prefWidth;

    public Boolean getEditable() {
        return editable.get();
    }

    public void setEditable(Boolean editable) {
        this.editable.set(editable);
    }

    public Property<Boolean> editableProperty() {
        return editable;
    }

    public Boolean getVisible() {
        return visible.get();
    }

    public void setVisible(Boolean visible) {
        this.visible.set(visible);
    }

    public Property<Boolean> visibleProperty() {
        return visible;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public Double getPrefWidth() {
        return prefWidth.get();
    }

    public void setPrefWidth(Double prefWidth) {
        this.prefWidth.set(prefWidth);
    }

    public Property<Double> prefWidthProperty() {
        return prefWidth;
    }

    public ObservableList<TableColumnModel> getChildren() {
        return children;
    }

    public String getCellFactoryClass() {
        return cellFactoryClass.get();
    }

    public void setCellFactoryClass(String cellFactoryClass) {
        this.cellFactoryClass.set(cellFactoryClass);
    }

    public Property<String> cellFactoryClassProperty() {
        return cellFactoryClass;
    }

}
