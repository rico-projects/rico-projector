package dev.rico.internal.projector.ui;


import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class IdentifiableModel {
    private Property<String> id;
    private ObservableList<String> properties;
    private Property<String> reference;

    public static boolean equalsById(IdentifiableModel m1, IdentifiableModel m2) {
        return m1 != null && m2 != null && m1.getId().equals(m2.getId());
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public Property<String> idProperty() {
        return id;
    }

    public ObservableList<String> getProperties() {
        return properties;
    }

    public String getReference() {
        return reference.get();
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    public Property<String> referenceProperty() {
        return reference;
    }
}
