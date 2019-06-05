package dev.rico.client.projector.mixed;

import com.github.rodionmoiseev.c10n.C10N;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class EditableListCell<T> extends ListCell<T> {
    private static EditableListCellC10n C10 = C10N.get(EditableListCellC10n.class);
    public static EventType<ListCellEvent> EDITABLE_LIST_CELL_ALL = new EventType<>("EDITABLE_LIST_CELL_ALL");
    public static EventType<ListCellEvent> EDITABLE_LIST_CELL_EDIT = new EventType<>(EDITABLE_LIST_CELL_ALL, "EDITABLE_LIST_CELL_EDIT");
    public static EventType<ListCellEvent> EDITABLE_LIST_CELL_SAVE = new EventType<>(EDITABLE_LIST_CELL_ALL, "EDITABLE_LIST_CELL_SAVE");
    public static EventType<ListCellEvent> EDITABLE_LIST_CELL_CANCEL = new EventType<>(EDITABLE_LIST_CELL_ALL, "EDITABLE_LIST_CELL_CANCEL");
    private final Button editButton = new Button("", new ImageView(Image.COG));
    private final Button okButton = new Button("", new ImageView(Image.OK));
    private final Button cancelButton = new Button("", new ImageView(Image.CANCEL));
    private final BorderPane layout = new BorderPane();
    private final ListCellSkin<T> skin;

    public EditableListCell(ListCellSkin<T> skin) {
        this.skin = Objects.requireNonNull(skin);
        skin.setSkinnable(this);
        editableProperty().bind(skin.editableByClickingProperty());

        setPrefWidth(0);
        layout.setCenter(skin.createContent());
        layout.setMaxWidth(Double.MAX_VALUE);

        VBox box = new VBox(okButton, cancelButton);
        box.setSpacing(4);
        layout.setRight(new StackPane(box, editButton));

        editButton.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setHalignment(editButton, HPos.RIGHT);
        editButton.visibleProperty().bind(selectedProperty().and(editingProperty().not()));
        editButton.getStyleClass().add("activeButton");
        editButton.setTooltip(new Tooltip(C10.editEntry()));
        okButton.setTooltip(new Tooltip(C10.apply()));
        okButton.visibleProperty().bind(editingProperty());
        cancelButton.setTooltip(new Tooltip(C10.cancel()));
        cancelButton.visibleProperty().bind(editingProperty());

        editButton.setOnAction(event -> {
            ContextMenu menu = new ContextMenu();
            skin.createContextItems().forEach(item -> menu.getItems().add(item));
            menu.show(editButton, Side.LEFT, 25, 0);
        });
        okButton.setOnAction(event -> {
            T item = getItem();
            skin.commit(item);
            commitEdit(item);
        });
        cancelButton.setOnAction(event -> cancelEdit());
        layout.prefWidthProperty().bind(widthProperty());
        setGraphic(layout);

        editingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                skin.reset(getItem());
                skin.takeFocus();
            }
        });
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        skin.clearContent();
        if (!empty && item != null) {
            setGraphic(layout);
            skin.setContent(item);
        } else {
            setGraphic(null);
        }
    }

    public void fireEvent(EventType<ListCellEvent> eventType) {
        T item = getItem();
        updateItem(item, false);
        Event.fireEvent(getListView(), new ListCellEvent<>(item, eventType));
    }


    public static class ListCellEvent<T> extends Event {
        private final T item;

        public ListCellEvent(T item, EventType<? extends Event> eventType) {
            super(eventType);
            this.item = item;
        }

        public T getItem() {
            return item;
        }
    }
}
