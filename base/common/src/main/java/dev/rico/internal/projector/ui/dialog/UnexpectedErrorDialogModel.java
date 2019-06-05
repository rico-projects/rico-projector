package dev.rico.internal.projector.ui.dialog;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class UnexpectedErrorDialogModel extends DialogModel {
    private Property<String> exceptionText;

    public String getExceptionText() {
        return exceptionText.get();
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText.set(exceptionText);
    }

    public Property<String> exceptionTextProperty() {
        return exceptionText;
    }
}
