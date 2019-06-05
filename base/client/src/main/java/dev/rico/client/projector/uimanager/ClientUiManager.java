package dev.rico.client.projector.uimanager;

import com.google.common.base.Strings;
import dev.rico.client.Client;
import dev.rico.client.projector.mixed.*;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.client.remoting.Param;
import dev.rico.common.projector.mixed.CommonUiHelper;
import dev.rico.common.projector.mixed.DocumentData;
import dev.rico.common.projector.ui.*;
import dev.rico.common.projector.ui.autocompletion.AutoCompleteModel;
import dev.rico.common.projector.ui.box.HBoxModel;
import dev.rico.common.projector.ui.box.VBoxModel;
import dev.rico.common.projector.ui.breadcrumbbar.BreadCrumbBarModel;
import dev.rico.common.projector.ui.cardpane.CardPaneItemModel;
import dev.rico.common.projector.ui.cardpane.CardPaneModel;
import dev.rico.common.projector.ui.checklistview.CheckListViewModel;
import dev.rico.common.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.common.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.common.projector.ui.container.ItemListContainerModel;
import dev.rico.common.projector.ui.dialog.*;
import dev.rico.common.projector.ui.flowpane.FlowPaneModel;
import dev.rico.common.projector.ui.gridpane.GridPaneModel;
import dev.rico.common.projector.ui.listselectionview.ListSelectionViewModel;
import dev.rico.common.projector.ui.listview.ListViewItemModel;
import dev.rico.common.projector.ui.listview.ListViewModel;
import dev.rico.common.projector.ui.menubutton.MenuButtonItemModel;
import dev.rico.common.projector.ui.menubutton.MenuButtonModel;
import dev.rico.common.projector.ui.menuitem.MenuItemModel;
import dev.rico.common.projector.ui.migpane.MigPaneModel;
import dev.rico.common.projector.ui.nestedmenubutton.NestedMenuButtonModel;
import dev.rico.common.projector.ui.propertysheet.PropertySheetModel;
import dev.rico.common.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.common.projector.ui.splitpane.SplitPaneModel;
import dev.rico.common.projector.ui.table.*;
import dev.rico.common.projector.ui.tabpane.TabPaneModel;
import dev.rico.core.functional.Binding;
import dev.rico.core.http.HttpClient;
import dev.rico.core.http.RequestMethod;
import dev.rico.internal.core.Assert;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import impl.org.controlsfx.ImplUtils;
import impl.org.controlsfx.skin.DecorationPane;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.tbee.javafx.scene.layout.MigPane;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.rico.client.projector.uimanager.TextField.configureTextInputControl;
import static dev.rico.client.remoting.FXBinder.bind;
import static dev.rico.client.remoting.FXWrapper.wrapList;
import static dev.rico.client.remoting.FXWrapper.wrapStringProperty;
import static java.util.Objects.requireNonNull;


public class ClientUiManager {
    protected final ControllerProxy<? extends ManagedUiModel> controllerProxy;
    private final PostProcessor postProcessor;
    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();
    private final WeakHashMap<String, Node> idToNodeMap = new WeakHashMap<>(); // TODO: String ist hier "weak" -> Problem!
    private final SimpleObjectProperty<Node> root = new SimpleObjectProperty<>();
    private final Function<String, Node> customComponentSupplier;
    private DolphinEventHandler handler;
    private ChangeListener<Boolean> listener;

    public ClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this(controllerProxy, null, null, null);
    }

    public ClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy, DolphinEventHandler handler, PostProcessor postProcessor, Function<String, Node> customComponentSupplier) {
        this.controllerProxy = controllerProxy;
        this.handler = handler;
        if (customComponentSupplier == null) {
            customComponentSupplier = (type) -> new Label("Placeholder for '" + type + "'");
        }
        this.customComponentSupplier = customComponentSupplier;
        if (postProcessor == null) {
            postProcessor = (id, model, node) -> {
            };
        }
        this.postProcessor = postProcessor;

        ManagedUiModel model = controllerProxy.getModel();
        model.rootProperty().onChanged(evt -> createRoot(evt.getNewValue()));
        createRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
            ItemModel oldDialog = event.getOldValue();
            ItemModel newDialog = event.getNewValue();
            if (newDialog instanceof CustomDialogModel) {
                createCustomDialog((CustomDialogModel) newDialog);
            } else if (newDialog instanceof SaveFileDialogModel) {
                createSaveFileDialog((SaveFileDialogModel) newDialog);
            } else if (newDialog instanceof InfoDialogModel) {
                createInfoDialog((InfoDialogModel) newDialog);
            } else if (newDialog instanceof ConfirmationDialogModel) {
                createConfirmationDialog((ConfirmationDialogModel) newDialog);
            } else if (newDialog instanceof UnexpectedErrorDialogModel) {
                createUnexpectedErrorDialog((UnexpectedErrorDialogModel) newDialog);
            } else if (newDialog instanceof QualifiedErrorDialogModel) {
                createQualifiedErrorDialog((QualifiedErrorDialogModel) newDialog);
            } else if (newDialog instanceof ShutdownDialogModel
                    && !(oldDialog instanceof ShutdownDialogModel)) {
                // Die if-Abfrage verhindert endlose Stapel mit ShutDown-Dialogen!
                createShutdownDialog((ShutdownDialogModel) newDialog);
            }
        }));

        transferFocus(model.getFocusedItem());
        model.focusedItemProperty().onChanged(evt -> transferFocus(evt.getNewValue()));

        model.eventProperty().onChanged(evt -> {
            requireNonNull(this.handler, "Received event, but there is no EventHandler installed");
            this.handler.onEvent(evt.getNewValue());
        });
    }

    private void createCustomDialog(CustomDialogModel newDialog) {
        Dialog<Boolean> dialog = new Dialog<>();
//        dialog.setDialogPane(new DialogPane() {
//            @Override
//            protected Node createButton(ButtonType buttonType) {
//                final Button button = new Button(buttonType.getText());
//                final ButtonBar.ButtonData buttonData = buttonType.getButtonData();
//                ButtonBar.setButtonData(button, buttonData);
//                button.setDefaultButton(buttonType != null && buttonData.isDefaultButton());
//                button.setCancelButton(buttonType != null && buttonData.isCancelButton());
//                button.addEventHandler(ActionEvent.ACTION, ae -> {
//                    if (ae.isConsumed()) return;
//                    if (dialog != null) {
//                        dialog.impl_setResultAndClose(buttonType, true);
//                    }
//                });
//            }
//        });
        dialog.setResizable(true);
        Optional<Node> nodeOptional = Optional.ofNullable(newDialog.getOwner()).map(modelToNodeMap::get);
        Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(dialog::initOwner);
        dialog.setTitle(newDialog.getTitle());
        dialog.setHeaderText(newDialog.getHeaderText());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Node content = createNode(newDialog.getContent());
        DecorationPane decorationPane = new DecorationPane();
        decorationPane.setRoot(content);
        dialog.getDialogPane().setContent(decorationPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        FXBinder.bind(okButton.disableProperty()).to(newDialog.okayEnabledProperty(), aBoolean -> aBoolean != null && !aBoolean);
        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK);
        if (newDialog.getCheckAction() != null) {
            okButton.addEventFilter(ActionEvent.ACTION, new ActionEventEventHandler(newDialog, okButton));
        }
        dialog.showAndWait().ifPresent(aBoolean -> {
            if (aBoolean) {
                controllerProxy.invoke(newDialog.getOkayAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(nodeOptional.orElse(null), throwable));
            }
        });
    }

    private void createSaveFileDialog(SaveFileDialogModel newDialog) {
        FileChooser chooser = new FileChooser();
        if (newDialog.getDirectory() != null) {
            File folder = new File(newDialog.getDirectory());
            if (!convertFolder(folder).isEmpty()) {
                chooser.setInitialDirectory(folder);
            }
        }
        chooser.setInitialFileName(newDialog.getFileName());
        File saveFile = chooser.showSaveDialog(findWindowOptional(newDialog).orElse(null));
        if (saveFile != null && !saveFile.isDirectory()) {
            try {
                FileUtils.writeByteArrayToFile(saveFile, newDialog.getSaveThis().getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createUnexpectedErrorDialog(UnexpectedErrorDialogModel model) {
        UnexpectedErrorDialog dialog = new UnexpectedErrorDialog();
        configureDialog(dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        dialog.showAndWait();
    }

    private static String convertFolder(File folder) {
        if (folder != null) {
            if (folder.isDirectory()) {
                try {
                    return folder.getCanonicalPath();
                } catch (IOException e) {
                    return "";
                }
            } else {
                return "";
            }
        }
        return "";
    }

    private void createConfirmationDialog(ConfirmationDialogModel newDialog) {
        Objects.requireNonNull(newDialog.getOkayAction());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<Node> nodeOptional = configureDialog(alert, newDialog);
        alert.setHeaderText(newDialog.getHeaderText());
        alert.setContentText(newDialog.getContentText());
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                controllerProxy.invoke(newDialog.getOkayAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(nodeOptional.orElse(null), throwable));
            }
        });
    }

    private void createInfoDialog(InfoDialogModel newDialog) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        configureDialog(alert, newDialog);
        alert.setHeaderText(newDialog.getHeaderText());
        alert.setContentText(newDialog.getContentText());
        alert.showAndWait();
    }

    private Optional<Node> configureDialog(Alert alert, DialogModel model) {
        Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(modelToNodeMap::get);
        Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(alert::initOwner);
        alert.setTitle(model.getTitle());
        return nodeOptional;
    }

    void setHandler(DolphinEventHandler handler) {
        this.handler = handler;
    }

    private void transferFocus(ItemModel focusThis) {
        Node node = modelToNodeMap.get(focusThis);
        if (node != null) {
            node.requestFocus();
        }
    }

    private void createRoot(ItemModel itemModel) {
        root.set(createNode(itemModel));
    }

    Node createNode(ItemModel itemModel) {
        try {
            if (itemModel != null && itemModel.getId() != null && idToNodeMap.containsKey(itemModel.getId())) {
                return idToNodeMap.get(itemModel.getId());
            }
            Node newNode;
            if (itemModel instanceof TabPaneModel) {
                newNode = createTabPane((TabPaneModel) itemModel);
            } else if (itemModel instanceof PasswordFieldModel) {
                newNode = createPasswordField((PasswordFieldModel) itemModel);
            } else if (itemModel instanceof MigPaneModel) {
                newNode = createMigPane((MigPaneModel) itemModel);
            } else if (itemModel instanceof CheckListViewModel) {
                newNode = createCheckListView((CheckListViewModel) itemModel);
            } else if (itemModel instanceof SegmentedButtonModel) {
                newNode = createSegmentedButton((SegmentedButtonModel) itemModel);
            } else if (itemModel instanceof SplitMenuButtonModel) {
                newNode = createSplitMenuButton((SplitMenuButtonModel) itemModel);
            } else if (itemModel instanceof ListSelectionViewModel) {
                newNode = createListSelectionView((ListSelectionViewModel) itemModel);
            } else if (itemModel instanceof NotificationPaneModel) {
                newNode = createNotificationPane((NotificationPaneModel) itemModel);
            } else if (itemModel instanceof CustomComponentModel) {
                newNode = createCustomComponent((CustomComponentModel) itemModel);
            } else if (itemModel instanceof NestedMenuButtonModel) {
                newNode = createNestedMenuButton((NestedMenuButtonModel) itemModel);
            } else if (itemModel instanceof ListViewModel) {
                newNode = createListView((ListViewModel) itemModel);
            } else if (itemModel instanceof HiddenSidesPaneModel) {
                newNode = createHiddenSidesPane((HiddenSidesPaneModel) itemModel);
            } else if (itemModel instanceof CardPaneModel) {
                newNode = createCardPane((CardPaneModel) itemModel);
            } else if (itemModel instanceof UploadButtonModel) {
                newNode = createUploadButton((UploadButtonModel) itemModel);
            } else if (itemModel instanceof FlowPaneModel) {
                newNode = createFlowPane((FlowPaneModel) itemModel);
            } else if (itemModel instanceof SeparatorModel) {
                newNode = createSeparator((SeparatorModel) itemModel);
            } else if (itemModel instanceof TableModel) {
                newNode = createTable((TableModel) itemModel);
            } else if (itemModel instanceof BreadCrumbBarModel) {
                newNode = createBreadCrumbBar((BreadCrumbBarModel) itemModel);
            } else if (itemModel instanceof FuelFieldModel) {
                newNode = createFuelFieldModel((FuelFieldModel) itemModel);
            } else if (itemModel instanceof PaxCodeFieldModel) {
                newNode = createPaxCodeFieldModel((PaxCodeFieldModel) itemModel);
            } else if (itemModel instanceof MenuButtonModel) {
                newNode = createMenuButton((MenuButtonModel) itemModel);
            } else if (itemModel instanceof ScrollPaneModel) {
                newNode = createScrollPane((ScrollPaneModel) itemModel);
            } else if (itemModel instanceof ChoiceBoxModel) {
                newNode = createChoiceBox((ChoiceBoxModel) itemModel);
            } else if (itemModel instanceof AutoCompleteModel) {
                newNode = createAutoCompleteField((AutoCompleteModel) itemModel);
            } else if (itemModel instanceof DateTimeFieldModel) {
                newNode = createDateField((DateTimeFieldModel) itemModel);
            } else if (itemModel instanceof MessagePlaceholder) {
                newNode = createMessagePlaceholder((MessagePlaceholder) itemModel);
            } else if (itemModel instanceof HyperlinkModel) {
                newNode = createHyperlink((HyperlinkModel) itemModel);
            } else if (itemModel instanceof ImageViewModel) {
                newNode = createImageView((ImageViewModel) itemModel);
            } else if (itemModel instanceof ProgressIndicatorModel) {
                newNode = createProgressIndicator((ProgressIndicatorModel) itemModel);
            } else if (itemModel instanceof TextAreaModel) {
                newNode = createTextArea((TextAreaModel) itemModel);
            } else if (itemModel instanceof PropertySheetModel) {
                newNode = createPropertySheet((PropertySheetModel) itemModel);
            } else if (itemModel instanceof DocumentViewModel) {
                newNode = createDocumentView((DocumentViewModel) itemModel);
            } else if (itemModel instanceof ToolBarModel) {
                newNode = createToolBar((ToolBarModel) itemModel);
            } else if (itemModel instanceof SplitPaneModel) {
                newNode = createSplitPane((SplitPaneModel) itemModel);
            } else if (itemModel instanceof BorderPaneModel) {
                newNode = createBorderPane((BorderPaneModel) itemModel);
            } else if (itemModel instanceof GridPaneModel) {
                newNode = createGridPane(((GridPaneModel) itemModel));
            } else if (itemModel instanceof ToggleItemModel) {
                newNode = createToggleItem(((ToggleItemModel) itemModel));
            } else if (itemModel instanceof SingleItemContainerModel) {
                newNode = createSingleItemContainer(((SingleItemContainerModel) itemModel));
            } else if (itemModel instanceof ItemListContainerModel) {
                newNode = createListItemContainer(((ItemListContainerModel) itemModel));
            } else if (itemModel instanceof LabelModel) {
                newNode = createLabel((LabelModel) itemModel);
            } else if (itemModel instanceof ButtonModel) {
                newNode = createButton((ButtonModel) itemModel);
            } else if (itemModel instanceof TextFieldModel) {
                newNode = createTextField((TextFieldModel) itemModel);
            } else if (itemModel == null) {
                return null;
            } else {
                throw new IllegalArgumentException("Unknown node type: " + itemModel);
            }
            configureNode(newNode, itemModel);
            modelToNodeMap.put(itemModel, newNode);
            idToNodeMap.put(itemModel.getId(), newNode);
            itemModel.idProperty().onChanged(evt -> {
                String oldId = findInMap(newNode);
                String newId = evt.getNewValue();
                idToNodeMap.put(newId, newNode);
                idToNodeMap.remove(oldId);
                postProcessor.postProcess(newId, itemModel, newNode);
            });
            postProcessor.postProcess(itemModel.getId(), itemModel, newNode);
            if (itemModel instanceof WithPadding && newNode instanceof Region) {
                WithPadding withPadding = (WithPadding) itemModel;
                Region region = (Region) newNode;
                bind(region.paddingProperty()).to(withPadding.paddingProperty(), value -> value == null ? new Insets(0) : new Insets(withPadding.getPadding()));
            }
            if (newNode instanceof BreadCrumbBar) {
                ScrollPane scrollPane = new ScrollPane(new Group(newNode));
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setFitToHeight(true);
                scrollPane.setPannable(true);
                return scrollPane;
            }
            return newNode;
        } catch (Throwable t) {
            // TODO: Beschissener Workaround, weil exceptions hier sonst nirgends gelogged werden:
            // http://atlassian.sprouts.private:8080/browse/FLY-258
            t.printStackTrace();
            throw t;
        }
    }

    private Node createTabPane(TabPaneModel itemModel) {
        TabPane tabPane = new TabPane();
        // TODO: Tab-Inhalt und Tab-Text sind leider nicht an das jeweilige Model gebunden, sondern nur an den Item
        FXBinder.bind(tabPane.getTabs()).to(itemModel.getItems(), model -> {
            Tab tab = new Tab(model.getCaption(), createNode(model.getContent()));
            tab.setClosable(false);
            return tab;
        });
        FXBinder.bind(tabPane.sideProperty()).to(itemModel.sideProperty(), side -> side == null ? Side.TOP : side);
        return tabPane;
    }

    private Node createMigPane(MigPaneModel item) {
        MigPane migPane = new MigPane(item.getLayoutConstraints(), item.getColumnConstraints(), item.getRowConstraints());
        item.getItems().forEach(migPaneItemModel -> {
            Node child = createNode(migPaneItemModel.getItem());
            migPane.add(child, migPaneItemModel.getConstraints());
        });
        item.getItems().onChanged(evt -> {
            migPane.getChildren().clear();
            item.getItems().forEach(migPaneItemModel -> {
                Node child = createNode(migPaneItemModel.getItem());
                migPane.add(child, migPaneItemModel.getConstraints());
            });
        });
        return migPane;
    }

    private Node createSegmentedButton(SegmentedButtonModel itemModel) {
        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null && oldVal != null)
                oldVal.setSelected(true);
        });
        FXBinder.bind(segmentedButton.getButtons()).to(itemModel.getItems(), model -> (ToggleButton) createNode(model));
        return segmentedButton;
    }

    private void createQualifiedErrorDialog(QualifiedErrorDialogModel model) {
        QualifiedErrorDialog dialog = new QualifiedErrorDialog();
        configureDialog(dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        bind(dialog.headerTextProperty()).to(model.headerTextProperty());
        dialog.setContentText(model.getContentText());
        dialog.setRootCauseText(model.getRootCauseText());
        dialog.showAndWait();
    }

    private Optional<Window> findWindowOptional(DialogModel newDialog) {
        Optional<Node> nodeOptional = Optional.ofNullable(newDialog.getOwner()).map(modelToNodeMap::get);
        return nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
    }

    private Node createListSelectionView(ListSelectionViewModel itemModel) {
        return new ListSelectionView(itemModel);
    }

    private Node createCheckListView(CheckListViewModel itemModel) {
        return new CheckListView(itemModel);
    }

    private Node createCustomComponent(CustomComponentModel itemModel) {
        return customComponentSupplier.apply(itemModel.getType());
    }

    private Node createSplitMenuButton(SplitMenuButtonModel itemModel) {
        SplitMenuButton button = new SplitMenuButton();
        button.setOnAction(event -> controllerProxy.invoke(itemModel.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(button.textProperty()).to(itemModel.captionProperty());
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionAProperty(), itemModel.captionAProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionBProperty(), itemModel.captionBProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionCProperty(), itemModel.captionCProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionDProperty(), itemModel.captionDProperty(), button));
        return button;
    }

    private MenuItem createSpliteMenuMenuItem(Property<String> actionProperty, Property<String> captionProperty, SplitMenuButton button) {
        MenuItem menuItem = new MenuItem();
        menuItem.setVisible(actionProperty.get() != null);
        actionProperty.onChanged(evt -> menuItem.setVisible(evt.getNewValue() != null));
        menuItem.setOnAction(event -> controllerProxy.invoke(actionProperty.get()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(menuItem.textProperty()).to(captionProperty);
        return menuItem;
    }

    private void createShutdownDialog(ShutdownDialogModel model) {
        getRoot().getScene().getWindow().hide();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (model.getOwner() != null) {
            alert.initOwner(modelToNodeMap.get(model.getOwner()).getScene().getWindow());
        }
        alert.setTitle("Neustart der Software");
        alert.setHeaderText("sprouts fly office erhält gerade eine Aktualisierung.");
        alert.setContentText("Die Anwendung hat sich deshalb automatisch beendet.");
        alert.showAndWait();
        System.exit(0);
    }

    private Node createNestedMenuButton(NestedMenuButtonModel itemModel) {
        ButtonBase button = configureButton(itemModel, new MenuButton());
        bind(button.textProperty()).to(itemModel.captionProperty());
        bind(((MenuButton) button).getItems()).to(itemModel.getItems(), this::createNestedMenuItem);
        return button;
    }

    private MenuItem createNestedMenuItem(MenuItemModel model) {
        if (model.getItems().isEmpty()) {
            MenuItem menuItem = new MenuItem();
            bind(menuItem.graphicProperty()).to(model.graphicProperty(), this::createNode);
            bind(menuItem.textProperty()).to(model.captionProperty());
            menuItem.setOnAction(event -> {
                if (event.getTarget() == menuItem) {
                    event.consume();
                    createOnActionHandler(model).handle(event);
                }
            });
            postProcessor.postProcess(model.getId(), model, menuItem);
            return menuItem;
        } else {
            Menu menu = new Menu();
            menu.setOnAction(event -> {
                if (event.getTarget() == menu) {
                    event.consume();
                    menu.getParentPopup().hide();
                    createOnActionHandler(model).handle(event);
                }
            });
            bind(menu.graphicProperty()).to(model.graphicProperty(), this::createNode);
            bind(menu.textProperty()).to(model.captionProperty());
            bind(menu.getItems()).to(model.getItems(), this::createNestedMenuItem);
            postProcessor.postProcess(model.getId(), model, menu);
            return menu;
        }
    }

    private EventHandler<ActionEvent> createOnActionHandler(IdentifiableModel identifiableModel) {
        return createOnActionHandler("buttonClick", identifiableModel);
    }

    private EventHandler<ActionEvent> createOnActionHandler(String type, IdentifiableModel identifiableModel) {
        return event -> {
            event.consume();
            if (identifiableModel instanceof ButtonModel && ((ButtonModel) identifiableModel).getAction() != null) {
                String action = ((ButtonModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else if (identifiableModel instanceof ToggleItemModel && ((ToggleItemModel) identifiableModel).getAction() != null) {
                String action = ((ToggleItemModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else {
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Node createListView(ListViewModel itemModel) {
        ListView<ListViewItemModel> listView = new ListView<>();
        bind(listView.getItems()).to(itemModel.getItems());
        ClientUiHelper.bindWithSelectionModel(itemModel.selectedProperty(), listView.getSelectionModel());
        if (itemModel.getSelectedAction() != null) {
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> controllerProxy.invoke(itemModel.getSelectedAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(listView, throwable)));
        }
        bind(listView.cellFactoryProperty()).to(itemModel.rendererClassProperty(), className -> {
            if (className == null) return null;
            return view -> {
                try {
                    ListCellSkin<ListViewItemModel> cellSkin = (ListCellSkin<ListViewItemModel>)
                            Class.forName(className).getConstructor().newInstance();
                    cellSkin.setOwner(itemModel);
                    cellSkin.setControllerProxy(controllerProxy);
                    return new EditableListCell<>(cellSkin);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new IllegalArgumentException(e);
                }
            };
        });
        itemModel.rendererClassProperty().onChanged(evt -> listView.setCellFactory(view -> new EditableListCell<>(new DocumentTemplateListCellSkin())));
        return listView;
    }

    private Node createHiddenSidesPane(HiddenSidesPaneModel itemModel) {
        Callback<ItemModel, Node> convert = param -> {
            if (param == null) {
                return null;
            }
            return createNode(param);
        };
        HiddenSidesPane pane = new HiddenSidesPane();
        pane.setTop(convert.call(itemModel.getTop()));
        pane.setLeft(convert.call(itemModel.getLeft()));
        pane.setRight(convert.call(itemModel.getRight()));
        pane.setBottom(convert.call(itemModel.getBottom()));
        pane.setContent(convert.call(itemModel.getContent()));
        itemModel.topProperty().onChanged(evt -> pane.setTop(convert.call(evt.getNewValue())));
        itemModel.leftProperty().onChanged(evt -> pane.setLeft(convert.call(evt.getNewValue())));
        itemModel.rightProperty().onChanged(evt -> pane.setRight(convert.call(evt.getNewValue())));
        itemModel.bottomProperty().onChanged(evt -> pane.setBottom(convert.call(evt.getNewValue())));
//        itemModel.contentProperty().onChanged(evt -> pane.setContent(convert.call(evt.getNewValue())));
        bind(pane.contentProperty()).to(itemModel.contentProperty(), this::createNode);
        bind(pane.pinnedSideProperty()).to(itemModel.pinnedSideProperty());
        return pane;
    }

    private Node createCardPane(CardPaneModel model) {
        CardPane cardPane = new CardPane(model);
        bind(cardPane.getChildren()).to(model.getItems(),
                cardPaneItemModel -> createNode(cardPaneItemModel.getItem()));
        model.visibleItemProperty().onChanged(evt -> {
            CardPaneItemModel visibleOne = evt.getNewValue();
            model.getItems().forEach(cardPaneItemModel -> {
                if (cardPaneItemModel.getItem() != null) {
                    cardPaneItemModel.getItem().setVisible(false);
                }
            });
            if (visibleOne != null && visibleOne.getItem() != null) {
                visibleOne.getItem().setVisible(true);
            }
        });
        if (model.getVisibleItem() == null && !model.getItems().isEmpty()) {
            model.setVisibleItem(model.getItems().get(0));
        }
        return cardPane;
    }

    private Node createUploadButton(UploadButtonModel itemModel) {
        ButtonBase button = configureButton(itemModel, new Button());
        button.setOnAction(event -> onDoUpload(event, itemModel));
        return button;
    }

    private void onDoUpload(ActionEvent actionEvent, UploadButtonModel uploadButton) {
        Assert.requireNonNull(uploadButton.getUploadUrl(), "buttonModel.uploadUrl");
        Button sourceButton = (Button) actionEvent.getSource();
        FileChooser chooser = new FileChooser();
        File loadFile = chooser.showOpenDialog(sourceButton.getScene().getWindow());
        if (loadFile != null && loadFile.exists() && loadFile.isFile() && !loadFile.isDirectory()) {
            try {
                DocumentData documentData = DocumentData.from(loadFile);
                Object detectedMimeType = documentData.getMimeType();
                if (detectedMimeType == null || uploadButton.getAllowedMimeTypes().stream()
                        .noneMatch(mimeType -> mimeType.equals(detectedMimeType.toString()))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Das Dokument kann nicht verarbeitet werden.");
                    String allowedOnes = uploadButton.getAllowedMimeTypes().stream().collect(Collectors.joining("\n- ", "- ", ""));
                    if ("- ".equals(allowedOnes)) {
                        allowedOnes = "- (keine)";
                    }
                    alert.setContentText("Die Dokumentenverwaltung unterstützt die folgenden Dokument-Formate:\n\n" + allowedOnes
                            + "\n\nDie ausgewählte Datei wurde als '" + detectedMimeType + "' erkannt.");
                    alert.showAndWait();
                } else {
                    if (uploadButton.getOnUploadBeginAction() != null) {
                        doRemoteCall(uploadButton.getOnUploadBeginAction());
                    }
                    sourceButton.setDisable(true);
                    HttpClient httpClient = Client.getService(HttpClient.class);
                    httpClient.put(uploadButton.getUploadUrl())
                            .withContent(documentData.getContent())
                            .readString().onError(e -> {
                        sourceButton.setDisable(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Das Dokument wurde nicht gespeichert.");
                        alert.setContentText("Die Dokumentenverwaltung konnte das Dokument nicht im System ablegen.");
                        alert.showAndWait();
                        if (uploadButton.getOnUploadFailedAction() != null) {
                            doRemoteCall(uploadButton.getOnUploadFailedAction());
                        }
                    }).onDone(stringHttpResponse -> {
                        uploadButton.setStoreId(stringHttpResponse.getContent());
                        sourceButton.setDisable(false);
                        if (uploadButton.getOnUploadFinishedAction() != null) {
                            doRemoteCall(uploadButton.getOnUploadFinishedAction());
                        }
                    }).execute();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                throw new IllegalArgumentException(exception);
            }
        }
    }

    private void doRemoteCall(String remoteAction) {
        Objects.requireNonNull(remoteAction);
        controllerProxy.invoke(remoteAction)
                .exceptionally(throwable -> UnexpectedErrorDialog.showError(getRoot(), throwable));
    }

    private Node createFlowPane(FlowPaneModel model) {
        FlowPane pane = new FlowPane();
        bind(pane.vgapProperty()).to(model.vGapProperty());
        bind(pane.hgapProperty()).to(model.hGapProperty());
        bind(pane.orientationProperty()).to(model.orientationProperty());
        bind(pane.alignmentProperty()).to(model.alignmentProperty());
        bind(pane.getChildren()).to(model.getItems(), content -> createNode(content.getItem()));
        return pane;
    }

    private Node createSeparator(SeparatorModel itemModel) {
        Separator separator = new Separator();
        bind(separator.orientationProperty()).to(itemModel.orientationProperty());
        return separator;
    }

    private Node createTable(TableModel model) {
        TableView<TableRowModel> table = new TableView<>();
        bind(table.editableProperty()).to(model.editableProperty(), value -> value == null ? false : value);
        new IndexedJavaFXListBinder<>(table.getColumns()).bidirectionalTo(model.getColumns(), conversionInfo -> createTableColumn(table, conversionInfo), conversionInfo -> (TableColumnModel) conversionInfo.getInput().getUserData());

        // bidirektional, damit Tabellensortierung funktioniert
        bind(table.getItems()).bidirectionalTo(model.getRows());
        ClientUiHelper.bindWithSelectionModel(model.selectedRowProperty(), table.getSelectionModel());

        FXBinder.bind(table.placeholderProperty()).to(model.placeholderProperty(),
                itemModel -> itemModel == null ? new Label("Nichts gefunden.") : createNode(itemModel));

        return table;
    }

    private TableColumn<TableRowModel, ?> createTableColumn(TableView<TableRowModel> table, IndexedJavaFXListBinder.ConversionInfo<TableColumnModel> conversionInfo) {
        TableColumn<TableRowModel, Object> tableColumn = new TableColumn<>(conversionInfo.getInput().getCaption());

        new IndexedJavaFXListBinder<>(tableColumn.getColumns()).bidirectionalTo(conversionInfo.getInput().getChildren(), subConversionInfo -> createTableColumn(table, subConversionInfo), subConversionInfo -> (TableColumnModel) subConversionInfo.getInput().getUserData());

        table.setUserData(conversionInfo.getInput());
        bind(tableColumn.textProperty()).to(conversionInfo.getInput().captionProperty());
        tableColumn.setCellValueFactory(param -> FXWrapper.wrapObjectProperty(
                param.getValue().getCells().get(conversionInfo.getIndex()).valueProperty()));
        bind(tableColumn.prefWidthProperty()).to(conversionInfo.getInput().prefWidthProperty(), value -> value == null ? 80 : value);
        bind(tableColumn.editableProperty()).to(conversionInfo.getInput().editableProperty(), value -> value == null ? true : value);
        if (conversionInfo.getInput() instanceof TableInstantColumnModel) {
            TableInstantColumnModel columnModel = (TableInstantColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(column -> new TableCell<TableRowModel, Object>() {
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else if (columnModel.getFormatString() != null) {
                        setText(FormatterFactory.customFormat(
                                columnModel.getFormatString()).format((Instant) item));
                    } else {
                        setText(FormatterFactory.dateFormatter().format((Instant) item));
                    }
                }
            });
        } else if (conversionInfo.getInput() instanceof TableStringColumnModel) {
            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PlainStringConverter()));
            tableColumn.setOnEditCommit(
                    t -> {
                        Object newValue = t.getNewValue();
                        controllerProxy.invoke("onTableStringCommit",
                                new Param("row", t.getRowValue().getReference()),
                                new Param("column", conversionInfo.getInput().getReference()),
                                new Param("newValue", newValue)).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable));
                    }
            );
        } else if (conversionInfo.getInput() instanceof TableChoiceBoxColumnModel) {
            TableChoiceBoxColumnModel choiceBoxColumnModel = (TableChoiceBoxColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(new ChoiceBoxItemConverter(choiceBoxColumnModel.getItems()), (javafx.collections.ObservableList) FXWrapper.wrapList(choiceBoxColumnModel.getItems())));
            tableColumn.setOnEditCommit(t -> {
                        Object newValue = t.getNewValue();
                        controllerProxy.invoke("onTableChoiceBoxCommit",
                                new Param("row", t.getRowValue().getReference()),
                                new Param("column", conversionInfo.getInput().getReference()),
                                new Param("newValue", ((ChoiceBoxItemModel) newValue).getReference())).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable));
                    }
            );
        } else if (conversionInfo.getInput() instanceof TableCheckBoxColumnModel) {
            tableColumn.setCellValueFactory(param -> {
                TableRowModel row = param.getValue();
                BooleanProperty property = new SimpleBooleanProperty((Boolean) row.getCells().get(conversionInfo.getIndex()).getValue());
                property.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> controllerProxy.invoke("onTableCheckBoxCommit",
                        new Param("row", param.getValue().getReference()),
                        new Param("column", conversionInfo.getInput().getReference()),
                        new Param("newValue", newValue)).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable)));
                return (ObservableValue) property;
            });
            tableColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        }
        return tableColumn;
    }

    private Node createBreadCrumbBar(BreadCrumbBarModel model) {
        return new dev.rico.client.projector.uimanager.BreadCrumbBar(model, controllerProxy);
    }

    private Node createFuelFieldModel(FuelFieldModel model) {
        FuelField field = new FuelField(model);
        Binding textPropertyBinding = configureTextInputControl(controllerProxy, model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    private Node createPaxCodeFieldModel(PaxCodeFieldModel model) {
        PaxCodeField field = new PaxCodeField();
        Binding textPropertyBinding = configureTextInputControl(controllerProxy, model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    private Node createMenuButton(MenuButtonModel itemModel) {
        MenuButton button = new MenuButton();
        if (itemModel.getCaption() == null) {
            button.getStyleClass().add("activeButton");
            button.setGraphic(new ImageView(Image.COG));
        } else {
            bind(button.textProperty()).to(itemModel.captionProperty());
        }
        bind(button.getItems()).to(itemModel.getItems(), this::createMenuItem);
        return button;
    }

    private MenuItem createMenuItem(MenuButtonItemModel model) {
        MenuItem menuItem = new MenuItem();
        bind(menuItem.textProperty()).to(model.captionProperty());
        if (model.getAction() != null) {
            menuItem.setOnAction(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(null, throwable)));
        } else {
            menuItem.setOnAction(createOnActionHandler(model));
        }
        return menuItem;
    }

    private Node createNotificationPane(NotificationPaneModel itemModel) {
        NotificationPane pane = new NotificationPane();
        bind(pane.contentProperty()).to(itemModel.contentProperty(), this::createNode);
        bind(pane.textProperty()).to(itemModel.textProperty());
        itemModel.textProperty().onChanged(evt -> {
            if (evt.getNewValue() == null || evt.getNewValue().isEmpty()) {
                pane.hide();
            } else {
                pane.show();
            }
        });
        return pane;
    }

    private Node createScrollPane(ScrollPaneModel itemModel) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        bind(scrollPane.contentProperty()).to(itemModel.contentProperty(), this::createNode);
        return scrollPane;
    }

    private Node createChoiceBox(ChoiceBoxModel itemModel) {
        return new ManagedChoiceBox(itemModel, controllerProxy);
    }



    private Node createAutoCompleteField(AutoCompleteModel itemModel) {
        return new ServerBackedAutoCompletionField(controllerProxy, itemModel);
    }

    private Node createDateField(DateTimeFieldModel itemModel) {
        return new DateTimeField(itemModel);
    }

    private Node createMessagePlaceholder(MessagePlaceholder itemModel) {
        Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setTextFill(Color.RED);
        label.setPadding(new Insets(0, 0, 0, 10));
        label.setWrapText(true);
        return label;
    }

    private Node createHyperlink(HyperlinkModel itemModel) {
        ButtonBase button = configureButton(itemModel, new Hyperlink());
        installMonitoredAction(button, createOnActionHandler(itemModel));
        return button;
    }

    private Node createImageView(ImageViewModel itemModel) {
        ImageView imageView = new ImageView();
        bind(imageView.imageProperty()).to(itemModel.resourcePathProperty(), value -> value == null ? null : new javafx.scene.image.Image(
                Image.class.getResource(value.substring("classpath:".length())).toExternalForm()));
        bind(imageView.preserveRatioProperty()).to(itemModel.preserveRatioProperty(), value -> value == null ? true : value);
        bind(imageView.fitWidthProperty()).to(itemModel.fitWidthProperty());
        bind(imageView.fitHeightProperty()).to(itemModel.fitHeightProperty());
        return imageView;
    }

    private Node createProgressIndicator(ProgressIndicatorModel itemModel) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        bind(itemModel.waitingProperty()).to(progressIndicator.activeProperty());
        return progressIndicator;
    }

    private Node createDocumentView(DocumentViewModel itemModel) {
        DocumentViewer documentViewer = new DocumentViewer(this, itemModel);
        Consumer<DocumentData> consumer = documentData -> {
            documentViewer.closeDocument();
            if (documentData != null) {
                documentViewer.openDocument(documentData, "");
            }
        };
        consumer.accept(itemModel.getDocumentData());
        CommonUiHelper.subscribeWithOptional(itemModel.documentByIdProperty(), idOptional -> {
            documentViewer.closeDocument();
            idOptional.ifPresent(id -> loadDocumentFromServerAndShow(id, consumer));
        });
        itemModel.documentDataProperty().onChanged(evt -> consumer.accept(evt.getNewValue()));
        return documentViewer;
    }

    private void loadDocumentFromServerAndShow(String documentId, Consumer<DocumentData> onFinish) {
        AsyncSequence.doAsync(() -> {
            try {
                if (documentId == null) {
                    return null;
                }
                HttpClient httpClient = Client.getService(HttpClient.class);
                return DocumentData.from(httpClient.request(Configuration.getServerUrl() + "/api/document/get/" + documentId, RequestMethod.GET)
                        .withoutContent().readBytes()
                        .execute().get().getContent().get());
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }, onFinish, exception -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Dokument nicht verfügbar.");
            alert.setContentText("Das System konnte das Dokument nicht abrufen.");
            alert.showAndWait();
        });
    }

    private Node createSplitPane(SplitPaneModel splitPaneModel) {
        SplitPane splitPane = new SplitPane();
        new IndexedJavaFXListBinder<>(splitPane.getItems()).to(splitPaneModel.getItems(), info -> createDividerContent(splitPane, info));
        bind(splitPane.orientationProperty()).to(splitPaneModel.orientationProperty());
        return splitPane;
    }

    private Node createDividerContent(SplitPane
                                              splitPane, IndexedJavaFXListBinder.ConversionInfo<SplitPaneItemModel> info) {
        Node newNode = createNode(info.getInput().getContent());
        splitPane.setDividerPosition(info.getIndex(), info.getInput().getDividerPosition());
        info.getInput().contentProperty().onChanged(evt -> splitPane.getItems().set(info.getIndex(), createNode(evt.getNewValue())));
        return newNode;
    }

    private Node createToolBar(ToolBarModel itemModel) {
        ToolBar toolBar = new ToolBar();
        updateBindItems(itemModel.getItems(), toolBar.getItems());
        return toolBar;
    }

    private void updateBindItems
            (ObservableList<ItemModel> remoteItems, javafx.collections.ObservableList<Node> fxItems) {
        bind(fxItems).to(remoteItems, this::createNode);
    }

    private Node createBorderPane(BorderPaneModel itemModel) {
        Callback<ItemModel, Node> convert = param -> {
            if (param == null) {
                return null;
            }
            return createNode(param);
        };
        BorderPane pane = new BorderPane();
        pane.setTop(convert.call(itemModel.getTop()));
        pane.setLeft(convert.call(itemModel.getLeft()));
        pane.setRight(convert.call(itemModel.getRight()));
        pane.setBottom(convert.call(itemModel.getBottom()));
        pane.setCenter(convert.call(itemModel.getCenter()));
        itemModel.topProperty().onChanged(evt -> pane.setTop(convert.call(evt.getNewValue())));
        itemModel.leftProperty().onChanged(evt -> pane.setLeft(convert.call(evt.getNewValue())));
        itemModel.rightProperty().onChanged(evt -> pane.setRight(convert.call(evt.getNewValue())));
        itemModel.bottomProperty().onChanged(evt -> pane.setBottom(convert.call(evt.getNewValue())));
        itemModel.centerProperty().onChanged(evt -> pane.setCenter(convert.call(evt.getNewValue())));
        return pane;
    }

    private Node createPropertySheet(PropertySheetModel model) {
        return new PropertySheet(controllerProxy, model);
    }

    private void configureNode(Node configureThis, ItemModel from) {
        bind(configureThis.disableProperty()).to(from.disableProperty());
        bind(configureThis.visibleProperty()).to(from.visibleProperty());
        bind(configureThis.managedProperty()).to(from.managedProperty());
        from.getStyleClass().addAll(configureThis.getStyleClass());
        bind(configureThis.styleProperty()).to(from.styleProperty());
        bind(configureThis.getStyleClass()).bidirectionalTo(from.getStyleClass());
        if (configureThis instanceof Region) {
            Region region = (Region) configureThis;
            if (from.getPrefHeight() == null) {
                from.setPrefHeight(region.getPrefHeight());
            }
            if (from.getPrefWidth() == null) {
                from.setPrefWidth(region.getPrefWidth());
            }
            bind(region.prefWidthProperty()).bidirectionalToNumeric(from.prefWidthProperty());
            bind(region.prefHeightProperty()).bidirectionalToNumeric(from.prefHeightProperty());
            if (from.getMaxWidth() == null) {
                from.setMaxWidth(region.getMaxWidth());
            }
            if (from.getMaxHeight() == null) {
                from.setMaxHeight(region.getMaxHeight());
            }
            bind(region.maxWidthProperty()).bidirectionalToNumeric(from.maxWidthProperty());
            bind(region.maxHeightProperty()).bidirectionalToNumeric(from.maxHeightProperty());
        }
        updateValidationFeedback(configureThis, from);
        wrapList(from.getValidationMessages()).addListener((InvalidationListener) observable ->
                updateValidationFeedback(configureThis, from));
        listener = (observable, oldValue, newValue) -> {
            // TODO Vielleicht umstellen auf ManagedUiController.setFocusedItem() ???
            try {
                controllerProxy.invoke("receivedFocus", new Param("id", from.getId())).exceptionally(throwable -> UnexpectedErrorDialog.showError(configureThis, throwable));
            } catch (IllegalStateException e) {
                // Gerät sonst in eine Endlosfehlerschleife...
                configureThis.focusedProperty().removeListener(listener);
            }
        };
        configureThis.focusedProperty().addListener(listener);
    }

    private void updateValidationFeedback(Node node, ItemModel itemModel) {
        Decorator.removeAllDecorations(node);
        // TODO:  Überschreibt individuell im Server gesetzte tooltips
//node.setTooltip(null);
        Label display = (Label) modelToNodeMap.get(itemModel.getMessageDisplay());
        if (display != null) {
            display.setManaged(false);
            display.setVisible(false);
        }
        ObservableList<String> validationMessages = itemModel.getValidationMessages();
        if (!validationMessages.isEmpty()) {
            Decorator.addDecoration(node, new StyleClassDecoration("warning"));
            List<Node> children = ImplUtils.getChildren(node.getParent(), true);
            StringBuilder text = new StringBuilder();
            for (String constraintViolation : validationMessages) {
                text.append(constraintViolation).append("\n");
            }
            if (display != null) {
                display.setLabelFor(node);
                display.setManaged(true);
                display.setVisible(true);
                display.setText(text.toString());
            } else {// TODO:  Überschreibt individuell im Server gesetzte tooltips
//            node.setTooltip(new Tooltip(text.toString()));
            }
        }
    }

    private String findInMap(Node node) {
        requireNonNull(node);
        for (String id : idToNodeMap.keySet()) {
            if (idToNodeMap.get(id) == node) {
                return id;
            }
        }
        throw new IllegalArgumentException("Expected node '" + node + "' in idToNodeMap");
    }

    private Node createGridPane(GridPaneModel item) {
        GridPane gridPane = new GridPane();
//        Platform.runLater(() -> gridPane.setGridLinesVisible(true));
        bind(gridPane.hgapProperty()).to(item.hGapProperty());
        bind(gridPane.vgapProperty()).to(item.vGapProperty());
        bind(gridPane.getChildren()).to(item.getItems(), content -> {
            Node child = createNode(content.getItem());
            GridPane.setRowIndex(child, content.getRow());
            GridPane.setColumnIndex(child, content.getCol());
            GridPane.setRowSpan(child, content.getRowSpan());
            GridPane.setColumnSpan(child, content.getColSpan());
            GridPane.setHalignment(child, content.gethAlignment());
            GridPane.setValignment(child, content.getvAlignment());
            GridPane.setHgrow(child, content.gethGrow());
            GridPane.setVgrow(child, content.getvGrow());
            return child;
        });
        return gridPane;
    }

    private Node createToggleItem(ToggleItemModel item) {
        if (item instanceof RadioButtonModel) {
            return createRadioButton((RadioButtonModel) item);
        } else if (item instanceof CheckBoxModel) {
            return createCheckBox((CheckBoxModel) item);
        } else if (item instanceof ToggleButtonModel) {
            return createToggleButton((ToggleButtonModel) item);
        } else {
            throw new IllegalArgumentException("Unknown toggle type: " + item);
        }
    }

    private Node createRadioButton(RadioButtonModel item) {
        RadioButton button = new RadioButton();
        bind(button.selectedProperty()).bidirectionalTo(item.selectedProperty());
        bind(button.textProperty()).to(item.captionProperty());
        button.setOnAction(createOnActionHandler(item));
        return button;
    }

    private ToggleButton createToggleButton(ToggleButtonModel item) {
        ToggleButton button = new ToggleButton();
        bind(button.graphicProperty()).to(item.graphicProperty(), this::createNode);
        bind(button.selectedProperty()).bidirectionalTo(item.selectedProperty());
        bind(button.textProperty()).to(item.captionProperty());
        button.setOnAction(event -> {
            event.consume();
            if (Strings.isNullOrEmpty(item.getAction())) {
                controllerProxy.invoke("onToggleButtonAction", new Param("model", item), new Param("selected", button.isSelected())).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable));
            } else {
                controllerProxy.invoke(item.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable));
            }
        });
        return button;
    }

    private Node createCheckBox(CheckBoxModel item) {
        CheckBox checkBox = new CheckBox();
        bind(checkBox.selectedProperty()).bidirectionalTo(item.selectedProperty());
        bind(checkBox.textProperty()).to(item.captionProperty());
        checkBox.setOnAction(createOnActionHandler(item));
        return checkBox;
    }

    private Node createSingleItemContainer(SingleItemContainerModel container) {
        if (container instanceof TitledPaneModel) {
            return createTitledPane((TitledPaneModel) container);
        } else {
            throw new IllegalArgumentException("Unknown container type: " + container);
        }
    }

    private Node createTitledPane(TitledPaneModel pane) {
        TitledPane result = new TitledPane();
        bind(result.textProperty()).to(pane.titleProperty());
        bind(result.contentProperty()).to(pane.contentProperty(), this::createNode);
        return result;
    }

    private Node createButton(ButtonModel item) {
        ButtonBase button = configureButton(item, new Button());
        installMonitoredAction(button, createOnActionHandler(item));
        return button;
    }

    private ButtonBase configureButton(ButtonModel item, ButtonBase button) {
        bind(button.textProperty()).to(item.captionProperty());
        CommonUiHelper.subscribeWithOptional(item.tooltipProperty(), tooltipOptional -> {
            Tooltip tooltip = null;
            if (tooltipOptional.isPresent()) {
                tooltip = new Tooltip(tooltipOptional.get());
            }
            button.setTooltip(tooltip);
        });
        CommonUiHelper.subscribeWithOptional(item.imageProperty(), optionalImagePath -> {
            ImageView graphic = null;
            if (optionalImagePath.isPresent()) {
                String imagePath = "/image/" + optionalImagePath.get();
                URL resource = ClientUiManager.class.getResource(imagePath);
                requireNonNull(resource, "Could not find classpath resource '" + imagePath + "'");
                graphic = new ImageView(new javafx.scene.image.Image(resource.toExternalForm()));
                graphic.setPreserveRatio(true);
                FXBinder.bind(graphic.fitHeightProperty()).to(item.prefHeightProperty());
            }
            button.setGraphic(graphic);
        });
        return button;
    }

    private void installMonitoredAction(ButtonBase button, EventHandler<ActionEvent> onActionHandler) {
        List<EventHandler<ActionEvent>> handlers = new LinkedList<>();
        EventHandler<ActionEvent> actionEventEventHandler = event -> handlers.forEach(handler -> handler.handle(event));
        handlers.add(onActionHandler);
        if (button.getOnAction() != null) {
            handlers.add(button.getOnAction());
        }
        button.setOnAction(actionEventEventHandler);
        button.onActionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == actionEventEventHandler) {
                return;
            }
            button.setOnAction(actionEventEventHandler);
            handlers.clear();
            handlers.add(onActionHandler);
            if (newValue != null) {
                handlers.add(newValue);
            }
        });
    }

    private Node createPasswordField(PasswordFieldModel item) {
        PasswordField result = new PasswordField();
        bind(result.prefColumnCountProperty()).to(item.prefColumnCountProperty());
        configureTextInputControl(controllerProxy, item, result);
        return result;
    }

    private Node createTextField(TextFieldModel item) {
        return new TextField(controllerProxy, item);
    }

    private Node createTextArea(TextAreaModel model) {
        return new TextArea(controllerProxy, model);
    }


    private Node createListItemContainer(ItemListContainerModel item) {
        Pane pane;
        if (item instanceof HBoxModel) {
            pane = createHBox((HBoxModel) item);
        } else if (item instanceof VBoxModel) {
            pane = createVBox((VBoxModel) item);
        } else {
            throw new IllegalArgumentException("Unknown container type: " + item);
        }
        return pane;
    }

    private VBox createVBox(VBoxModel item) {
        VBox vBox = new VBox();
        bind(vBox.spacingProperty()).to(item.spacingProperty(), value -> value == null ? 0 : value);
        bind(vBox.alignmentProperty()).to(item.alignmentProperty());
        bind(vBox.getChildren()).to(item.getItems(), content -> {
            Node child = createNode(content.getItem());
            VBox.setVgrow(child, content.getvGrow());
            return child;
        });
        return vBox;
    }

    private HBox createHBox(HBoxModel item) {
        HBox hBox = new HBox();
        bind(hBox.spacingProperty()).to(item.spacingProperty(), value -> value == null ? 0 : value);
        bind(hBox.alignmentProperty()).to(item.alignmentProperty());
        bind(hBox.getChildren()).to(item.getItems(), content -> {
            Node child = createNode(content.getItem());
            HBox.setHgrow(child, content.gethGrow());
            return child;
        });
        return hBox;
    }

    private Node createLabel(LabelModel labelModel) {
        Label result = new Label(labelModel.getText());
        bind(result.textProperty()).to(labelModel.textProperty());
        bind(result.wrapTextProperty()).to(labelModel.wrapTextProperty(), value -> value == null ? true : value);
        bind(result.alignmentProperty()).to(labelModel.alignmentProperty());
        bind(result.textAlignmentProperty()).to(labelModel.textAlignmentProperty());
        return result;
    }

    public Node getRoot() {
        return root.get();
    }

    public SimpleObjectProperty<Node> rootProperty() {
        return root;
    }

    @SuppressWarnings("unchecked")
    public <T extends Node> T getNodeById(String id) {
        return requireNonNull((T) idToNodeMap.get(id), "Missing injected node with id: " + id);
    }

    public interface PostProcessor {
        void postProcess(String id, IdentifiableModel model, Object node);
    }

    private static class PlainStringConverter extends StringConverter<Object> {
        @Override
        public String toString(Object object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public Object fromString(String string) {
            return string;
        }
    }

    private static class BooleanConverter extends StringConverter<Object> {
        @Override
        public String toString(Object object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public Object fromString(String string) {
            return Boolean.valueOf(string);
        }
    }

    private static class ChoiceBoxItemConverter extends StringConverter<Object> {
        private final ObservableList<ChoiceBoxItemModel> items;

        ChoiceBoxItemConverter(ObservableList<ChoiceBoxItemModel> items) {
            this.items = items;
        }

        @Override
        public String toString(Object object) {
            if (object == null) return null;
            return ((ChoiceBoxItemModel) object).getCaption();
        }

        @Override
        public Object fromString(String caption) {
            return items.stream().filter(choiceBoxItemModel -> choiceBoxItemModel.getCaption().equals(caption)).findFirst().orElse(null);
        }
    }

    private class ActionEventEventHandler implements EventHandler<ActionEvent> {
        private final CustomDialogModel newDialog;
        private final Button okButton;
        private boolean invokeAction = true;
        private final Object lock = new Object();

        ActionEventEventHandler(CustomDialogModel newDialog, Button okButton) {
            this.newDialog = newDialog;
            this.okButton = okButton;
        }

        @Override
        public void handle(ActionEvent event) {
            synchronized (lock) {
                if (invokeAction) {
                    invokeAction = false;
                    event.consume();
                    Thread thread = new Thread(() -> {
                        CompletableFuture<Void> future = controllerProxy.invoke(newDialog.getCheckAction());
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            synchronized (lock) {
                                if (newDialog.getCheckSuccessful() != null && newDialog.getCheckSuccessful()) {
                                    okButton.fire();
                                } else
                                    invokeAction = true;
                            }
                        });
                    });
                    thread.start();
                }
            }
        }
    }
}
