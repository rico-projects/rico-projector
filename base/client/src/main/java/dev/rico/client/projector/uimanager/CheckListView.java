package dev.rico.client.projector.uimanager;

import dev.rico.common.projector.ui.checklistview.CheckListViewModel;
import dev.rico.common.projector.ui.listselectionview.ListSelectionViewItemModel;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.util.Comparator;

public class CheckListView extends BorderPane {

    private final CheckListViewModel checkListViewModel;
    private ListView<ListSelectionViewItemModel> listView;
    private TextField filterField;
    private final SortedList<ListSelectionViewItemModel> sortedList;

    CheckListView(CheckListViewModel checkListViewModel) {
        this.checkListViewModel = checkListViewModel;
        setTop(buildSearchField());
        setCenter(buildListView());

        BorderPane.setMargin(filterField, new Insets(0, 0, 5, 0));

        ObservableList<ListSelectionViewItemModel> availableItems = FXWrapper.wrapList(checkListViewModel.getAvailableItems());
        FilteredList<ListSelectionViewItemModel> filteredList = new FilteredList<>(availableItems);
        filterField.textProperty().addListener(observable -> filteredList.setPredicate(this::testMatch));

        sortedList = new SortedList<>(filteredList);
        sortedList.setComparator(buildComparator(checkListViewModel));
        listView.setItems(sortedList);

        FXBinder.bind(checkListViewModel.editableProperty()).bidirectionalTo(listView.editableProperty());
    }

    private Comparator<ListSelectionViewItemModel> buildComparator(CheckListViewModel checkListViewModel) {
        Comparator<ListSelectionViewItemModel> comparing = Comparator.comparing(item -> !checkListViewModel.getSelectedItems().contains(item));
        return comparing.thenComparing(ListSelectionViewItemModel::getCaption);
    }

    private Node buildListView() {
        listView = new ListView<>();
        listView.setCellFactory(CheckBoxListCell.forListView(this::buildItemSelectionObservable, new ItemToStringConverter()));
        return listView;
    }

    private Node buildSearchField() {
        filterField = new TextField();
        filterField.setPromptText("Einträge filtern");
        return filterField;
    }

    private boolean testMatch(ListSelectionViewItemModel listSelectionViewItemModel) {
        return listSelectionViewItemModel.getCaption().toLowerCase().contains(filterField.getText().toLowerCase());
    }

    private ObservableValue<Boolean> buildItemSelectionObservable(ListSelectionViewItemModel item) {
        boolean selected = checkListViewModel.getSelectedItems().contains(item);
        SimpleBooleanProperty property = new SimpleBooleanProperty(selected);
        property.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkListViewModel.getSelectedItems().add(item);
            } else {
                checkListViewModel.getSelectedItems().remove(item);
            }
            sortedList.setComparator(buildComparator(checkListViewModel));
        });
        return property;
    }

    private static class ItemToStringConverter extends StringConverter<ListSelectionViewItemModel> {
        @Override
        public String toString(ListSelectionViewItemModel object) {
            return object.getCaption();
        }

        @Override
        public ListSelectionViewItemModel fromString(String string) {
            return null;
        }
    }
}
