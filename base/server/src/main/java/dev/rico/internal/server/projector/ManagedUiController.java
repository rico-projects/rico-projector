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
package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingContext;

public interface ManagedUiController {

    ItemModel buildUi();

    ServerUiManager ui();

    RemotingContext getSession();

    BeanManager getBeanManager();

    ManagedUiModel getModel();

    default <T extends IdentifiableModel> Retained<T> retain(final T model) {
        return new Keeper<>(this, model);
    }

}
