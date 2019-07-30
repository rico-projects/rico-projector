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
package dev.rico.internal.projector.ui;

import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ItemModel<M extends ItemModel> extends IdentifiableModel {
    private Property<Boolean> disable;
    private Property<Boolean> visible;
    private Property<Boolean> managed;
    private Property<Double> maxWidth;
    private Property<Double> maxHeight;
    private Property<Double> prefWidth;
    private Property<Double> prefHeight;
    private Property<Double> minWidth;
    private Property<Double> minHeight;
    private Property<String> style;
    private Property<ItemModel> messageDisplay;
    private ObservableList<String> styleClass;
    private ObservableList<String> validationMessages;

    public M withMaxWidth(double maxWidth) {
        setMaxWidth(maxWidth);
        return (M) this;
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

    public Double getPrefHeight() {
        return prefHeight.get();
    }

    public void setPrefHeight(Double prefHeight) {
        this.prefHeight.set(prefHeight);
    }

    public Property<Double> prefHeightProperty() {
        return prefHeight;
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

    public Boolean getManaged() {
        return managed.get();
    }

    public void setManaged(Boolean managed) {
        this.managed.set(managed);
    }

    public Property<Boolean> managedProperty() {
        return managed;
    }

    public String getStyle() {
        return style.get();
    }

    public void setStyle(String style) {
        this.style.set(style);
    }

    public Property<String> styleProperty() {
        return style;
    }

    public ObservableList<String> getStyleClass() {
        return styleClass;
    }

    public ObservableList<String> getValidationMessages() {
        return validationMessages;
    }

    public ItemModel getMessageDisplay() {
        return messageDisplay.get();
    }

    public Property<ItemModel> messageDisplayProperty() {
        return messageDisplay;
    }

    public Boolean getDisable() {
        return disable.get();
    }

    public void setDisable(Boolean disable) {
        this.disable.set(disable);
    }

    public Property<Boolean> disableProperty() {
        return disable;
    }

    public Double getMaxWidth() {
        return maxWidth.get();
    }

    public void setMaxWidth(Double maxWidth) {
        this.maxWidth.set(maxWidth);
    }

    public Property<Double> maxWidthProperty() {
        return maxWidth;
    }

    public Double getMinWidth() {
        return minWidth.get();
    }

    public void setMinWidth(Double minWidth) {
        this.minWidth.set(minWidth);
    }

    public Property<Double> minWidthProperty() {
        return minWidth;
    }

    public Double getMinHeight() {
        return minHeight.get();
    }

    public void setMinHeight(Double minHeight) {
        this.minHeight.set(minHeight);
    }

    public Property<Double> minHeightProperty() {
        return minHeight;
    }

    public Double getMaxHeight() {
        return maxHeight.get();
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight.set(maxHeight);
    }

    public Property<Double> maxHeightProperty() {
        return maxHeight;
    }
}
