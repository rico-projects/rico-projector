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


import dev.rico.projector.model.WithText;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TextFieldModel extends ItemModel implements WithPromptText, WithText<TextFieldModel> {
    private Property<String> action;
    private Property<Integer> actionDelay;
    private Property<String> text;
    private Property<String> promptText;
    private Property<Integer> prefColumnCount;
    private Property<Boolean> editable;

    public String getPromptText() {
        return promptText.get();
    }

    public void setPromptText(String promptText) {
        this.promptText.set(promptText);
    }

    public Property<String> promptTextProperty() {
        return promptText;
    }

    public Property<String> textProperty() {
        return text;
    }

    public Integer getPrefColumnCount() {
        return prefColumnCount.get();
    }

    public void setPrefColumnCount(Integer prefColumnCount) {
        this.prefColumnCount.set(prefColumnCount);
    }

    public Property<Integer> prefColumnCountProperty() {
        return prefColumnCount;
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

    public Integer getActionDelay() {
        return actionDelay.get();
    }

    public void setActionDelay(Integer actionDelay) {
        this.actionDelay.set(actionDelay);
    }

    public Property<Integer> ActionDelayProperty() {
        return actionDelay;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

}
