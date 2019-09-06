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
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.dialog.QualifiedErrorDialogModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.ClientSessionExecutor;
import dev.rico.server.remoting.Param;
import dev.rico.server.remoting.RemotingAction;
import dev.rico.server.remoting.RemotingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class AbstractManagedUiController implements ManagedUiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractManagedUiController.class);

    private final ClientSessionExecutor sessionExecutor;
    private final BeanManager beanManager;
    private final RemotingContext session;
    private ServerUiManager uiManager;

    public AbstractManagedUiController(final BeanManager beanManager, final RemotingContext session) {
        this.beanManager = Assert.requireNonNull(beanManager, "beanManager");
        this.session = Assert.requireNonNull(session, "session");
        sessionExecutor = session.createSessionExecutor();
    }

    protected <V> V callInUi(final Callable<V> callInUiThread) {
        try {
            return sessionExecutor.callLaterInClientSession(callInUiThread).get();
        } catch (final InterruptedException | ExecutionException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(exception);
        }
    }

    protected void runInUi(final Runnable runInUiThread) {
        try {
            sessionExecutor.runLaterInClientSession(runInUiThread).get();
        } catch (final InterruptedException | ExecutionException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(exception);
        }
    }

    protected void runAndForgetInUi(final Runnable runInUiThread) {
        sessionExecutor.runLaterInClientSession(runInUiThread).exceptionally(throwable -> {
            LOGGER.error("Error in execution", throwable);
            return null;
        });
    }

    @PostConstruct
    protected void init() {
        try {
            postConstruct();
            uiManager = createUiManager();
            initModel(getModel());
            getModel().setRoot(buildUi());
            initRootPane();
        } catch (final Exception exception) {
            showUnexpectedError(exception);
        }
    }

    protected ServerUiManager createUiManager() {
        return new ServerUiManager(beanManager);
    }

    protected void postConstruct() {

    }

    protected void initModel(final ManagedUiModel model) {

    }

    protected void initRootPane() {

    }

    protected void showUnexpectedError(final Throwable stackTrace) {
        getModel().setIsWorking(false);
        LOGGER.error("Unexpected error", stackTrace);
        final String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        getModel().showDialog(ui().unexpectedErrorDialog(getModel().getRoot(), exceptionText));
    }

    protected void showQualifiedError(final String headerText, final String contentText, final Throwable stackTrace) {
        getModel().setIsWorking(false);
        LOGGER.error("Qualified error", stackTrace);
        final String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        final QualifiedErrorDialogModel dialog = ui().qualifiedErrorDialog(getModel().getRoot(), headerText, contentText, exceptionText);
        dialog.setRootCauseText(ExceptionUtils.getRootCauseMessage(stackTrace));
        getModel().showDialog(dialog);
    }

    @Override
    public ServerUiManager ui() {
        return uiManager;
    }

    @Override
    public RemotingContext getSession() {
        return session;
    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }

    @RemotingAction
    public void receivedFocus(@Param("id") final String id) {
        processReceivedFocus(id);
    }

    protected void processReceivedFocus(final String fieldId) {
    }
}
