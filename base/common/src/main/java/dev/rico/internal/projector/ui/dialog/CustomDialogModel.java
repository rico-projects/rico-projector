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
package dev.rico.internal.projector.ui.dialog;


import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class CustomDialogModel extends DialogModel {
    private Property<String> headerText;
    private Property<ItemModel> content;
    private Property<Boolean> okayEnabled;
    private Property<String> okayAction;
    private Property<String> checkAction;
    private Property<Boolean> checkSuccessfull;

    public Boolean getOkayEnabled() {
        return okayEnabled.get();
    }

    public void setOkayEnabled(Boolean okayEnabled) {
        this.okayEnabled.set(okayEnabled);
    }

    public Property<Boolean> okayEnabledProperty() {
        return okayEnabled;
    }

    public Boolean getCheckSuccessful() {
        return checkSuccessfull.get();
    }

    public void setCheckSuccessful(Boolean checkSuccessful) {
        this.checkSuccessfull.set(checkSuccessful);
    }

    public Property<Boolean> checkSuccessfulProperty() {
        return checkSuccessfull;
    }

    public String getHeaderText() {
        return headerText.get();
    }

    public void setHeaderText(String headerText) {
        this.headerText.set(headerText);
    }

    public Property<String> headerTextProperty() {
        return headerText;
    }

    public ItemModel getContent() {
        return content.get();
    }

    public void setContent(ItemModel content) {
        this.content.set(content);
    }

    public Property<ItemModel> contentTextProperty() {
        return content;
    }

    public String getOkayAction() {
        return okayAction.get();
    }

    public void setOkayAction(String okayAction) {
        this.okayAction.set(okayAction);
    }

    public Property<String> okayActionProperty() {
        return okayAction;
    }

    public String getCheckAction() {
        return checkAction.get();
    }

    public void setCheckAction(String checkAction) {
        this.checkAction.set(checkAction);
    }

    public Property<String> okayCheckProperty() {
        return checkAction;
    }
}
