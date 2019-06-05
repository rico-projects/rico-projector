package dev.rico.internal.projector.ui;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.mixed.DocumentData;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@ForRemoval
@RemotingBean
public class DocumentViewModel extends ItemModel {
    private Property<String> documentById;
    private Property<DocumentData> documentData;
    private Property<Boolean> showProgress;
    private ObservableList<ItemModel> additionalToolBarItems;

    public DocumentData getDocumentData() {
        return documentData.get();
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData.set(documentData);
    }

    public Property<DocumentData> documentDataProperty() {
        return documentData;
    }

    public String getDocumentById() {
        return documentById.get();
    }

    public Property<String> documentByIdProperty() {
        return documentById;
    }

    public void setDocumentById(String documentById) {
        this.documentById.set(documentById);
    }

    public ObservableList<ItemModel> getAdditionalToolBarItems() {
        return additionalToolBarItems;
    }
}
