package dev.rico.internal.projector.ui.listview;


import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ListViewItemModel extends IdentifiableModel {
    private ObservableList<String> content;
    private Property<String> title;
    private Property<String> detail1;
    private Property<String> detail2;

    public String getDetail2() {
        return detail2.get();
    }

    public void setDetail2(String detail2) {
        this.detail2.set(detail2);
    }

    public Property<String> detail2Property() {
        return detail2;
    }

    public ObservableList<String> getContent() {
        return content;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Property<String> titleProperty() {
        return title;
    }

    public String getDetail1() {
        return detail1.get();
    }

    public void setDetail1(String detail1) {
        this.detail1.set(detail1);
    }

    public Property<String> detail1Property() {
        return detail1;
    }
}
