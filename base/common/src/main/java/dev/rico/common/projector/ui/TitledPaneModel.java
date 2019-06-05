package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TitledPaneModel extends SingleItemContainerModel {
    private Property<String> title;

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Property<String> titleProperty() {
        return title;
    }
}
