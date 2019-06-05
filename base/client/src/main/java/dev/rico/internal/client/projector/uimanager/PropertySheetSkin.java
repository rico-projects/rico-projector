package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.DateTimeFieldModel;
import dev.rico.internal.projector.ui.FuelFieldModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.PaxCodeFieldModel;
import dev.rico.internal.projector.ui.propertysheet.*;
import dev.rico.internal.client.projector.uimanager.IndexedJavaFXListBinder.ConversionInfo;
import dev.rico.internal.client.projector.uimanager.spinner.IntegerStringConverter;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.ValueChangeEvent;
import dev.rico.remoting.ValueChangeListener;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.miginfocom.layout.CC;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.fxmisc.easybind.EasyBind;
import org.tbee.javafx.scene.layout.MigPane;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static dev.rico.client.remoting.FXWrapper.*;
import static javafx.scene.input.DataFormat.PLAIN_TEXT;


class PropertySheetSkin extends BehaviorSkinBase<PropertySheet, PropertySheetBehavior> {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;
    private final PropertySheetModel model;

    PropertySheetSkin(PropertySheet control, ControllerProxy<? extends ManagedUiModel> controllerProxy, PropertySheetModel model) {
        super(control, new PropertySheetBehavior(control, new ArrayList<>()));
        this.controllerProxy = controllerProxy;
        this.model = model;
        initialize();
    }

    private void initialize() {
        VBox vBox = new VBox();
        if (model.getSkin() == null || model.getSkin() == PropertySheetModel.Skin.ExpandableGroups) {
            FXBinder.bind(vBox.getChildren()).to(model.getGroups(), this::createGroup);
        } else {
            javafx.collections.ObservableList<PropertySheetItemModel> allItems = FXCollections.observableArrayList();
            vBox.getChildren().add(new PropertyPane(controllerProxy, allItems));
            FXBinder.bind(FXCollections.observableArrayList()).to(model.getGroups(), group -> createOneGroup(allItems, group));
        }
        getChildren().add(vBox);
    }

    private Node createOneGroup(List<PropertySheetItemModel> allItems, PropertySheetItemGroupModel group) {
        // Trick: null fügt einen Separator ein
        allItems.addAll(group.getItems());
        allItems.add(null);
        return new Label("dummy");
    }

    private Node createGroup(PropertySheetItemGroupModel group) {
        if (model.getSkin() == null || model.getSkin() == PropertySheetModel.Skin.ExpandableGroups) {
            TitledPane titledPane = new TitledPane();
            titledPane.textProperty().bind(wrapStringProperty(group.nameProperty()));
            BooleanProperty other = wrapBooleanProperty(group.expandedProperty());
            titledPane.expandedProperty().bindBidirectional(other);
            PropertyPane propertyPane = createPropertyPane(group.getItems());
            titledPane.setContent(propertyPane);
            return titledPane;
        } else {
            return createPropertyPane(group.getItems());
        }
    }

    private PropertyPane createPropertyPane(ObservableList<PropertySheetItemModel> items) {
        return new PropertyPane(controllerProxy, items);
    }

    private interface ValueChangeListenerWithBufferedValue<T> extends ValueChangeListener<T> {
        T getBufferedValue();
    }

    private static class ItemData {
        public MigPane getMigPane() {
            return migPane;
        }

        public Label getLabel() {
            return label;
        }

        public Node getValue() {
            return value;
        }

        public Tooltip getTooltip() {
            return tooltip;
        }

        private final MigPane migPane;
        private final Label label;
        private final Node value;
        private final Tooltip tooltip;

        private ItemData(MigPane migPane, Label label, Node value, Tooltip tooltip) {
            this.migPane = migPane;
            this.label = label;
            this.value = value;
            this.tooltip = tooltip;
        }
    }

    private class PropertyPane extends MigPane {
        private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

        PropertyPane(ControllerProxy<? extends ManagedUiModel> controllerProxy, ObservableList<PropertySheetItemModel> items) {
            super("", "[][:340:]");
            this.controllerProxy = controllerProxy;
            new IndexedJavaFXListBinder<>(FXCollections.observableArrayList()).to(items, info -> createRow(this, info));
        }

        PropertyPane(ControllerProxy<? extends ManagedUiModel> controllerProxy, javafx.collections.ObservableList<PropertySheetItemModel> allItems) {
            super("", "[][:340:]");
            this.controllerProxy = controllerProxy;
            allItems.addListener((ListChangeListener<? super PropertySheetItemModel>) change -> {
                while (change.next()) {
                    for (PropertySheetItemModel additem : change.getAddedSubList()) {
                        createRow(this, new ConversionInfo(-1, additem));
                    }
                }
            });
        }

        private ItemData createRow(MigPane parent, ConversionInfo<PropertySheetItemModel> info) {
            if (info.getInput() == null) {
//                parent.add(new Separator(), "spanx 2, grow, wrap");
                return null;
            }
            Label label = new Label();
            label.textProperty().bind(wrapStringProperty(info.getInput().captionProperty()));
            Tooltip tooltip = new Tooltip();
            tooltip.textProperty().bind(wrapStringProperty(info.getInput().descriptionProperty()));
            label.setTooltip(tooltip);
            Node viewerComponent = createViewerComponent(info.getInput(), label);
            ItemData itemData = new ItemData(parent, label, viewerComponent, new Tooltip());
            updateValidationFeedback(itemData, info.getInput().getValidationMessages());
            wrapList(info.getInput().getValidationMessages()).addListener((InvalidationListener) observable ->
                    updateValidationFeedback(itemData, info.getInput().getValidationMessages()));
            parent.add(label);
            parent.add(viewerComponent, new CC().grow().wrap());

            ValueChangeListener valueChangeListener = evt -> {
                boolean visible = model.getHideNullItems() == null || !model.getHideNullItems()
                        || (info.getInput().getAlwaysVisible() != null && info.getInput().getAlwaysVisible())
                        || (model.getEditing() != null && model.getEditing())
                        || (model.getHideNullItems() && info.getInput().getStringValue() != null && !info.getInput().getStringValue().trim().isEmpty());

                if (info.getInput().getHideOnEdit() != null && info.getInput().getHideOnEdit() && model.getEditing() != null && model.getEditing()) {
                    visible = false;
                }

                label.setVisible(visible);
                label.setManaged(visible);
                viewerComponent.setVisible(visible);
                viewerComponent.setManaged(visible);
            };

            CommonUiHelper.subscribe(info.getInput().hideOnEditProperty(), valueChangeListener);
            CommonUiHelper.subscribe(info.getInput().stringValueProperty(), valueChangeListener);
            CommonUiHelper.subscribe(model.editingProperty(), valueChangeListener);

            return itemData;
        }

        private Node createViewerComponent(PropertySheetItemModel itemModel, Label label) {
            Objects.requireNonNull(itemModel);
            Node result;
            if (itemModel instanceof PropertySheetDateItemModel) {
                result = createDateField((PropertySheetDateItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetTimeItemModel) {
                result = createTimeField((PropertySheetTimeItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetEetItemModel) {
                result = createEetField((PropertySheetEetItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetTextAreaItemModel) {
                result = createTextArea((PropertySheetTextAreaItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetTextFieldItemModel) {
                result = createTextField((PropertySheetTextFieldItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetCheckBoxItemModel) {
                result = createCheckBox((PropertySheetCheckBoxItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetInstantItemModel) {
                result = createInstantField((PropertySheetInstantItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetAutoCompleteItemModel) {
                result = createAutoCompleteField((PropertySheetAutoCompleteItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetFuelItemModel) {
                result = createFuelField((PropertySheetFuelItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetPaxCodeItemModel) {
                result = createPaxCodeField((PropertySheetPaxCodeItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetChoiceBoxItemModel) {
                result = createChoiceBox((PropertySheetChoiceBoxItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetSpinnerBoxItemModel) {
                result = createSpinnerBox((PropertySheetSpinnerBoxItemModel) itemModel);
            } else if (itemModel instanceof PropertySheetCheckListViewItemModel) {
                result = createCheckListView((PropertySheetCheckListViewItemModel) itemModel);
            } else {
                throw new IllegalArgumentException("Unknown subtype of PropertySheetItemModel: " + itemModel.getClass());
            }
            Label readOnlyComponent = new Label();
            addCopyToClipboardFeature(label, readOnlyComponent);
            addCopyToClipboardFeature(readOnlyComponent, readOnlyComponent);
            readOnlyComponent.setPadding(new Insets(4, 8, 4, 8));
            readOnlyComponent.textProperty().bind(wrapStringProperty(itemModel.stringValueProperty()));
            BooleanBinding editingProperty = wrapBooleanProperty(model.editingProperty()).and(wrapBooleanProperty(itemModel.readOnlyProperty()).not());
            readOnlyComponent.visibleProperty().bind(editingProperty.not());
            result.visibleProperty().bind(editingProperty);
            result.managedProperty().bind(editingProperty);
            StackPane stackPane = new StackPane(result, readOnlyComponent);
            stackPane.setAlignment(Pos.CENTER_LEFT);
            return stackPane;
        }

        private void updateValidationFeedback(ItemData itemData, ObservableList<String> messages) {
            Decorator.removeAllDecorations(itemData.getValue());
            Tooltip.uninstall(itemData.getValue(), itemData.getTooltip());
            if (!messages.isEmpty()) {
                Decorator.addDecoration(itemData.getValue(), new StyleClassDecoration("warning"));
                StringBuilder text = new StringBuilder();
                for (String constraintViolation : messages) {
                    text.append(constraintViolation).append("\n");
                }
                itemData.getTooltip().setText(text.toString());
                Tooltip.install(itemData.getValue(), itemData.getTooltip());
            }
        }

        private Node createTextField(PropertySheetTextFieldItemModel itemModel) {
            TextField field = new TextField(controllerProxy, itemModel.getField());
            field.editableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()).not());
            return field;
        }

        private Node createTextArea(PropertySheetTextAreaItemModel itemModel) {
            TextArea field = new TextArea(controllerProxy, itemModel.getField());
            field.editableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()).not());
            return field;
        }

        private Node createEetField(PropertySheetEetItemModel itemModel) {
            EetField field = new EetField(controllerProxy, itemModel.getField());
            field.editableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()).not());
            return field;
        }

        private Node createCheckBox(PropertySheetCheckBoxItemModel itemModel) {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(wrapBooleanProperty(itemModel.getField().selectedProperty()));
            checkBox.disableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()));
            return checkBox;
        }

        private Node createCheckListView(PropertySheetCheckListViewItemModel itemModel) {
            CheckListView listView = new CheckListView(itemModel.getField());
            listView.disableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()));
            return listView;
        }

        private Node createSpinnerBox(PropertySheetSpinnerBoxItemModel itemModel) {
            Spinner<Integer> spinner = new Spinner<>(0, Integer.MAX_VALUE, 0);
            IntegerStringConverter.createFor(spinner);
            spinner.setEditable(true);
            spinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            spinner.getValueFactory().valueProperty().bindBidirectional(wrapObjectProperty(itemModel.valueProperty()));
            spinner.disableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()));
            return spinner;
        }

        // TODO: create*Field() sind fast alle gleich
        private Node createInstantField(PropertySheetInstantItemModel itemModel) {
            DateTimeField dateTimeField = new DateTimeField(itemModel.getField());

            InstantValueChangeListenerWithBufferedValue listener = new InstantValueChangeListenerWithBufferedValue(itemModel);
            CommonUiHelper.subscribe(itemModel.valueProperty(), listener);
            CommonUiHelper.subscribe(itemModel.fieldProperty(), evt -> {
                DateTimeFieldModel newValue = evt.getNewValue();
                if (newValue != null) {
                    newValue.setDate(listener.bufferedDate);
                    newValue.setTime(listener.bufferedTime);
                }
            });

            return dateTimeField;
        } // TODO: create*Field() sind fast alle gleich

        private Node createDateField(PropertySheetDateItemModel itemModel) {
            return new DateTimeField(itemModel.getField());
        }

        private Node createTimeField(PropertySheetTimeItemModel itemModel) {
            return new DateTimeField(itemModel.getField());
        }

        // TODO: create*Field() sind fast alle gleich
        private Node createAutoCompleteField(PropertySheetAutoCompleteItemModel itemModel) {
            return new ServerBackedAutoCompletionField(controllerProxy, itemModel.getField());
        }

        // TODO: create*Field() sind fast alle gleich
        private Node createFuelField(PropertySheetFuelItemModel itemModel) {
            FuelField fuelField = new FuelField(itemModel.getField()) {
                private Binding<String> binding;

                {
                    // Referenz speichern verhindert, dass die GarbageCollection das Binding entfernt
                    binding = EasyBind.select(FXWrapper.wrapObjectProperty(itemModel.fieldProperty()))
                            .selectObject(fuelFieldModel -> FXWrapper.wrapObjectProperty(fuelFieldModel.textProperty()));
                    binding.addListener((observable, oldValue, newValue) -> itemModel.getField().setText(newValue));
                }
            };

            fuelField.textProperty().bindBidirectional(wrapStringProperty(itemModel.getField().textProperty()));

            ValueChangeListenerWithBufferedValue<String> listener = new ValueChangeListenerWithBufferedValue<String>() {
                private String bufferedValue;

                public String getBufferedValue() {
                    return bufferedValue;
                }

                @Override
                public void valueChanged(ValueChangeEvent<? extends String> evt) {
                    bufferedValue = evt.getNewValue();
                    if (itemModel.getField() != null) {
                        itemModel.getField().setText(bufferedValue);
                    }
                }
            };
            CommonUiHelper.subscribe(itemModel.getField().textProperty(), listener);
            CommonUiHelper.subscribe(itemModel.fieldProperty(), evt -> {
                FuelFieldModel newValue = evt.getNewValue();
                if (newValue != null) {
                    newValue.setText(listener.getBufferedValue());
                }
            });

            return fuelField;
        }

        // TODO: create*Field() sind fast alle gleich
        private Node createPaxCodeField(PropertySheetPaxCodeItemModel itemModel) {
            PaxCodeField paxCodeField = new PaxCodeField() {
                private Binding<String> binding;

                {
                    // Referenz speichern verhindert, dass die GarbageCollection das Binding entfernt
                    binding = EasyBind.select(FXWrapper.wrapObjectProperty(itemModel.fieldProperty()))
                            .selectObject(paxCodeFieldModel -> FXWrapper.wrapObjectProperty(paxCodeFieldModel.textProperty()));
                    binding.addListener((observable, oldValue, newValue) -> itemModel.setValue(newValue));
                }
            };

            paxCodeField.textProperty().bindBidirectional(wrapStringProperty(itemModel.valueProperty()));

            ValueChangeListenerWithBufferedValue<String> listener = new ValueChangeListenerWithBufferedValue<String>() {
                private String bufferedValue;

                public String getBufferedValue() {
                    return bufferedValue;
                }

                @Override
                public void valueChanged(ValueChangeEvent<? extends String> evt) {
                    bufferedValue = evt.getNewValue();
                    if (itemModel.getField() != null) {
                        itemModel.getField().setText(bufferedValue);
                    }
                }
            };
            CommonUiHelper.subscribe(itemModel.valueProperty(), listener);
            CommonUiHelper.subscribe(itemModel.fieldProperty(), evt -> {
                PaxCodeFieldModel newValue = evt.getNewValue();
                if (newValue != null) {
                    newValue.setText(listener.getBufferedValue());
                }
            });

            return paxCodeField;
        }

        private Node createChoiceBox(PropertySheetChoiceBoxItemModel itemModel) {
            ManagedChoiceBox choiceBox = new ManagedChoiceBox(itemModel.getField(), controllerProxy);
            choiceBox.disableProperty().bind(wrapBooleanProperty(itemModel.readOnlyProperty()));
            return choiceBox;
        }

        private class InstantValueChangeListenerWithBufferedValue implements ValueChangeListenerWithBufferedValue<Instant> {
            private final PropertySheetInstantItemModel itemModel;
            private LocalDate bufferedDate;
            private LocalTime bufferedTime;

            InstantValueChangeListenerWithBufferedValue(PropertySheetInstantItemModel itemModel) {
                this.itemModel = itemModel;
                itemModel.getField().dateProperty().onChanged(evt -> {
                    bufferedDate = evt.getNewValue();
                    updateInstant();
                });
                itemModel.getField().timeProperty().onChanged(evt -> {
                    bufferedTime = evt.getNewValue();
                    updateInstant();
                });
            }

            private void updateInstant() {
                itemModel.setValue(getBufferedValue());
            }

            public Instant getBufferedValue() {
                if (bufferedDate == null || bufferedTime == null) return null;
                return LocalDateTime.of(bufferedDate, bufferedTime).toInstant(ZoneOffset.UTC);
            }

            @Override
            public void valueChanged(ValueChangeEvent<? extends Instant> evt) {
                Instant instant = evt.getNewValue();
                if (itemModel.getField() != null && instant != null) {
                    ZonedDateTime atZone = instant.atZone(ZoneOffset.UTC);
                    bufferedDate = LocalDate.from(atZone);
                    itemModel.getField().setDate(bufferedDate);
                    bufferedTime = LocalTime.from(atZone);
                    itemModel.getField().setTime(bufferedTime);
                }
            }
        }
    }

    private void addCopyToClipboardFeature(Label clickableComponent, Label textProvider) {
        clickableComponent.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String text = textProvider.getText();
                if (text != null && !text.trim().isEmpty()) {
                    Clipboard.getSystemClipboard().setContent(Collections.singletonMap(PLAIN_TEXT, text));
                    ImageView graphic = new ImageView("/image/copy_icon.png");
                    graphic.setPreserveRatio(true);
                    graphic.setFitWidth(18);
                    graphic.getStyleClass().add("copy-icon");
                    Notifications.create().graphic(graphic).position(Pos.BOTTOM_RIGHT).title("Der Wert befindet sich jetzt im Clipboard").text(text).show();
                }
            }
        });
    }
}
