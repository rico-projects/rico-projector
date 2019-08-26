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
import dev.rico.internal.projector.ui.HyperlinkModel;
import javafx.scene.control.Hyperlink;

public class HyperlinkFactory extends ButtonBaseFactory<HyperlinkModel, Hyperlink> {

    @Override
    public Hyperlink create(final Projector projector, final HyperlinkModel model) {
        return createButtonBase(projector, model, new Hyperlink());
    }

    @Override
    public Class<HyperlinkModel> getSupportedType() {
        return HyperlinkModel.class;
    }
}
