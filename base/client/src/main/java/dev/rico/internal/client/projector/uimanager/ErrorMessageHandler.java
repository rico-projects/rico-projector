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
package dev.rico.internal.client.projector.uimanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ErrorMessageHandler {

    private static final ErrorMessageHandler INSTANCE = new ErrorMessageHandler();

    private final StringProperty stacktraceMessage = new SimpleStringProperty("Exception stacktrace:");

    private final StringProperty quiteApplicationText = new SimpleStringProperty("Quit application");

    private final StringProperty errorTitle = new SimpleStringProperty("Unexpected error");

    private final StringProperty errorMessage = new SimpleStringProperty("The application reported an unexpected error at runtime!");

    private ErrorMessageHandler() {
    }

    public static ErrorMessageHandler getInstance() {
        return INSTANCE;
    }

    public String getStacktraceMessage() {
        return stacktraceMessage.get();
    }

    public void setStacktraceMessage(final String stacktraceMessage) {
        this.stacktraceMessage.set(stacktraceMessage);
    }

    public StringProperty stacktraceMessageProperty() {
        return stacktraceMessage;
    }

    public String getQuiteApplicationText() {
        return quiteApplicationText.get();
    }

    public void setQuiteApplicationText(final String quiteApplicationText) {
        this.quiteApplicationText.set(quiteApplicationText);
    }

    public StringProperty quiteApplicationTextProperty() {
        return quiteApplicationText;
    }

    public String getErrorTitle() {
        return errorTitle.get();
    }

    public void setErrorTitle(final String errorTitle) {
        this.errorTitle.set(errorTitle);
    }

    public StringProperty errorTitleProperty() {
        return errorTitle;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }
}
