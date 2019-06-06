package to.remove;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.listview.ListViewItemModel;
import com.github.rodionmoiseev.c10n.C10N;
import dev.rico.client.remoting.FXBinder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static to.remove.EditableListCell.EDITABLE_LIST_CELL_ALL;

@ForRemoval
public class DocumentTemplateListCellSkin extends ListCellSkinBase<ListViewItemModel> {

    private static DocumentTemplateListCellSkinC10n C10 = C10N.get(DocumentTemplateListCellSkinC10n.class, Locale.GERMAN);

    public static EventType<EditableListCell.ListCellEvent> REPLACE_TEMPLATE_EVENT =
            new EventType<>(EDITABLE_LIST_CELL_ALL, "REPLACE_TEMPLATE_EVENT");
    public static EventType<EditableListCell.ListCellEvent> EXPORT_TEMPLATE_EVENT =
            new EventType<>(EDITABLE_LIST_CELL_ALL, "EXPORT_TEMPLATE_EVENT");
    public static EventType<EditableListCell.ListCellEvent> IMPORT_TEMPLATE_EVENT =
            new EventType<>(EDITABLE_LIST_CELL_ALL, "IMPORT_TEMPLATE_EVENT");
    public static EventType<EditableListCell.ListCellEvent> REMOVE_TEMPLATE_EVENT =
            new EventType<>(EDITABLE_LIST_CELL_ALL, "REMOVE_TEMPLATE_EVENT");

    private Label templateTypeLabel = new Label();
    private Label templateInstalledLabel = new Label();

    public DocumentTemplateListCellSkin() {
        editableByClickingProperty().set(false);
    }

    @Override
    public List<MenuItem> createContextItems() {
        EventHandler<ActionEvent> saveTemplateHandler = e -> getSkinnable().fireEvent(
                new EditableListCell.ListCellEvent<>(getItem(), REPLACE_TEMPLATE_EVENT));
        EventHandler<ActionEvent> exportTemplateHandler = e -> getSkinnable().fireEvent(
                new EditableListCell.ListCellEvent<>(getItem(), EXPORT_TEMPLATE_EVENT));
        EventHandler<ActionEvent> importTemplateHandler = e -> getSkinnable().fireEvent(
                new EditableListCell.ListCellEvent<>(getItem(), IMPORT_TEMPLATE_EVENT));
        EventHandler<ActionEvent> removeTemplateHandler = e -> getSkinnable().fireEvent(
                new EditableListCell.ListCellEvent<>(getItem(), REMOVE_TEMPLATE_EVENT));
        List<MenuItem> menu = new ArrayList<>();
        if (getItem().getId() == null) {
            menu.add(menuItem(C10.addTemplate(), saveTemplateHandler));
//            menu.add(menuItem(C10.importTemplate(), importTemplateHandler));
        } else {
            menu.add(menuItem(C10.overwriteTemplate(), saveTemplateHandler));
            menu.add(menuItem(C10.exportTemplate(), exportTemplateHandler));
//            menu.add(menuItem(C10.importTemplate(), importTemplateHandler));
        }
        return menu;
    }

    @Override
    public Node createContent() {
        GridPane layout = new GridPane();
        layout.add(templateTypeLabel, 0, 0);
        layout.add(templateInstalledLabel, 0, 1);
        return layout;
    }

    @Override
    public void takeFocus() {
    }

    @Override
    public void reset(ListViewItemModel item) {
    }

    @Override
    public void commit(ListViewItemModel newValue) {
    }

    public void setContent(ListViewItemModel item) {
        FXBinder.bind(templateTypeLabel.textProperty()).to(item.titleProperty());
        FXBinder.bind(templateInstalledLabel.textProperty()).to(item.detail1Property());
//        MonadicBinding<String> idBinding = EasyBind.map(FXWrapper.wrapObjectProperty(item.idProperty()), s -> s == null ? "Es wurde noch keine Vorlage hinterlegt." : "Die hinterlegte Vorlage ist aktiv.");
//        templateInstalledLabel.textProperty().bind(idBinding);
    }

    public void clearContent() {
        templateInstalledLabel.textProperty().unbind();
        templateInstalledLabel.setText(null);
        templateTypeLabel.textProperty().unbind();
        templateTypeLabel.setText(null);
    }
}
