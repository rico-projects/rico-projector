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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingContext;

public abstract class DelegatingManagedUiController implements ManagedUiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingManagedUiController.class);

    private ManagedUiController delegate;
    private Retained<ItemModel> thisUi;

    public DelegatingManagedUiController(final ManagedUiController delegate) {
        this.delegate = Assert.requireNonNull(delegate, "delegate");
    }

    protected DelegatingManagedUiController() {
    }

    public void setDelegate(final ManagedUiController delegate) {
        this.delegate = Assert.requireNonNull(delegate, "delegate");
    }

    public ItemModel getUi() {
        if (thisUi == null) {
            thisUi = retain(buildUi());
        }
        return thisUi.get();
    }

    @Override
    public ServerUiManager ui() {
        return delegate.ui();
    }

    @Override
    public RemotingContext getSession() {
        return delegate.getSession();
    }

    @Override
    public BeanManager getBeanManager() {
        return delegate.getBeanManager();
    }

    @Override
    public ManagedUiModel getModel() {
        return delegate.getModel();
    }

    protected <T> void doInParallel(final Supplier<T> backgroundJob, final Consumer<T> refreshJob, final Consumer<Exception> errorJob) {
        Assert.requireNonNull(backgroundJob, "backgroundJob");
        Assert.requireNonNull(refreshJob, "refreshJob");
        Assert.requireNonNull(errorJob, "errorJob");

        new Thread(() -> {
            try {
                T obj = backgroundJob.get();
                synchronized (obj) {
                    getSession().createSessionExecutor().runLaterInClientSession(() -> {
                        synchronized (obj) {
                            refreshJob.accept(obj);
                        }
                    });
                }
            } catch (Exception exception) {
                synchronized (exception) {
                    getSession().createSessionExecutor().runLaterInClientSession(() -> {
                        synchronized (exception) {
                            errorJob.accept(exception);
                        }
                    });
                }
            }
        }
        ).start();
    }

    protected <V> V callInUi(final Callable<V> callInUiThread) {
        try {
            return getSession().createSessionExecutor().callLaterInClientSession(callInUiThread).get();
        } catch (final InterruptedException | ExecutionException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(exception);
        }
    }

    protected void runInUi(final Runnable runInUiThread) {
        try {
            getSession().createSessionExecutor().runLaterInClientSession(runInUiThread).get();
        } catch (final InterruptedException | ExecutionException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(exception);
        }
    }

    protected void runAndForgetInUi(final Runnable runInUiThread) {
        getSession().createSessionExecutor().runLaterInClientSession(runInUiThread).exceptionally(throwable -> {
            LOGGER.error("Error during execution", throwable);
            return null;
        });
    }
}
