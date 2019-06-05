package dev.rico.common.projector.ui;

import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class UploadButtonModel extends ButtonModel {
    private Property<String> uploadUrl;
    private ObservableList<String> allowedMimeTypes;
    private Property<String> storeId;
    private Property<String> onUploadBeginAction;
    private Property<String> onUploadFinishedAction;
    private Property<String> onUploadFailedAction;

    public String getUploadUrl() {
        return uploadUrl.get();
    }

    public Property<String> uploadUrlProperty() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl.set(uploadUrl);
    }

    public String getOnUploadBeginAction() {
        return onUploadBeginAction.get();
    }

    public Property<String> onUploadBeginActionProperty() {
        return onUploadBeginAction;
    }

    public void setOnUploadBeginAction(String onUploadBeginAction) {
        this.onUploadBeginAction.set(onUploadBeginAction);
    }

    public String getOnUploadFinishedAction() {
        return onUploadFinishedAction.get();
    }

    public Property<String> onUploadFinishedActionProperty() {
        return onUploadFinishedAction;
    }

    public void setOnUploadFinishedAction(String onUploadFinishedAction) {
        this.onUploadFinishedAction.set(onUploadFinishedAction);
    }

    public String getOnUploadFailedAction() {
        return onUploadFailedAction.get();
    }

    public Property<String> onUploadFailedActionProperty() {
        return onUploadFailedAction;
    }

    public void setOnUploadFailedAction(String onUploadFailedAction) {
        this.onUploadFailedAction.set(onUploadFailedAction);
    }

    public ObservableList<String> getAllowedMimeTypes() {
        return allowedMimeTypes;
    }

    public String getStoreId() {
        return storeId.get();
    }

    public Property<String> storeIdProperty() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId.set(storeId);
    }
}
