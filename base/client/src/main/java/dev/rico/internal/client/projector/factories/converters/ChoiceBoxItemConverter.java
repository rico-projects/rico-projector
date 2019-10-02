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
package dev.rico.internal.client.projector.factories.converters;

import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.remoting.ObservableList;
import javafx.util.StringConverter;

public class ChoiceBoxItemConverter extends StringConverter<ChoiceBoxItemModel> {

    private final ObservableList<ChoiceBoxItemModel> items;

    public ChoiceBoxItemConverter(final ObservableList<ChoiceBoxItemModel> items) {
        this.items = items;
    }

    @Override
    public String toString(final ChoiceBoxItemModel object) {
        if (object == null)
            return null;
        return object.getCaption();
    }

    @Override
    public ChoiceBoxItemModel fromString(final String caption) {
        return items.stream().filter(choiceBoxItemModel -> choiceBoxItemModel.getCaption().equals(caption)).findFirst()
                .orElse(null);
    }
}