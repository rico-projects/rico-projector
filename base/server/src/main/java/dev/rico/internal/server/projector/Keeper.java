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

import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;

class Keeper<T extends IdentifiableModel> implements Retained<T> {
    private final ManagedUiController managedUiController;
    private final T keepThis;

    Keeper(final ManagedUiController managedUiController, final T keepThis) {
        this.keepThis = Assert.requireNonNull(keepThis, "keepThis");
        this.managedUiController = Assert.requireNonNull(managedUiController, "managedUiController");
    }

    Keeper() {
        this.managedUiController = null;
        this.keepThis = null;
    }

    public T get() {
        return keepThis;
    }

    @Override
    protected void finalize() {
        if (managedUiController != null && keepThis != null) {
            managedUiController.getSession().createSessionExecutor().runLaterInClientSession(
                    () -> managedUiController.getModel().getRetainedModels().remove(keepThis));
        }
    }
}
