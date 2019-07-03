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
