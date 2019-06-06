package dev.rico.internal.client.projector.uimanager;

import static dev.rico.client.remoting.FXBinder.bind;
import static dev.rico.client.remoting.FXWrapper.wrapList;
import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.tbee.javafx.scene.layout.MigPane;
import com.google.common.base.Strings;
import dev.rico.client.Client;
import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.FXWrapper;
import dev.rico.client.remoting.Param;
import dev.rico.core.http.HttpClient;
import dev.rico.core.http.RequestMethod;
import dev.rico.internal.client.projector.mixed.Configuration;
import dev.rico.internal.client.projector.mixed.FormatterFactory;
import dev.rico.internal.client.projector.mixed.ListCellSkin;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.BorderPaneModel;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.CheckBoxModel;
import dev.rico.internal.projector.ui.CustomComponentModel;
import dev.rico.internal.projector.ui.DateTimeFieldModel;
import dev.rico.internal.projector.ui.HyperlinkModel;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ImageViewModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.LabelModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.PasswordFieldModel;
import dev.rico.internal.projector.ui.RadioButtonModel;
import dev.rico.internal.projector.ui.ScrollPaneModel;
import dev.rico.internal.projector.ui.SeparatorModel;
import dev.rico.internal.projector.ui.SingleItemContainerModel;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.internal.projector.ui.TitledPaneModel;
import dev.rico.internal.projector.ui.ToggleButtonModel;
import dev.rico.internal.projector.ui.ToggleItemModel;
import dev.rico.internal.projector.ui.ToolBarModel;
import dev.rico.internal.projector.ui.WithPadding;
import dev.rico.internal.projector.ui.box.HBoxModel;
import dev.rico.internal.projector.ui.box.VBoxModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.internal.projector.ui.dialog.ConfirmationDialogModel;
import dev.rico.internal.projector.ui.dialog.CustomDialogModel;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import dev.rico.internal.projector.ui.dialog.InfoDialogModel;
import dev.rico.internal.projector.ui.dialog.QualifiedErrorDialogModel;
import dev.rico.internal.projector.ui.dialog.SaveFileDialogModel;
import dev.rico.internal.projector.ui.dialog.ShutdownDialogModel;
import dev.rico.internal.projector.ui.dialog.UnexpectedErrorDialogModel;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import dev.rico.internal.projector.ui.listview.ListViewItemModel;
import dev.rico.internal.projector.ui.listview.ListViewModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneModel;
import dev.rico.internal.projector.ui.table.TableCheckBoxColumnModel;
import dev.rico.internal.projector.ui.table.TableChoiceBoxColumnModel;
import dev.rico.internal.projector.ui.table.TableColumnModel;
import dev.rico.internal.projector.ui.table.TableModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.internal.projector.ui.table.TableStringColumnModel;
import dev.rico.internal.projector.ui.tabpane.TabPaneModel;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import to.remove.DocumentData;
import to.remove.DocumentTemplateListCellSkin;
import to.remove.DolphinEventHandler;
import to.remove.EditableListCell;
import to.remove.Image;
import to.remove.ui.HiddenSidesPaneModel;
import to.remove.ui.MessagePlaceholder;
import to.remove.ui.NotificationPaneModel;
import to.remove.ui.SegmentedButtonModel;
import to.remove.ui.SplitMenuButtonModel;
import to.remove.ui.UploadButtonModel;
import to.remove.ui.menubutton.MenuButtonItemModel;
import to.remove.ui.migpane.MigPaneModel;
import to.remove.ui.propertysheet.PropertySheetModel;
import to.remove.ui.table.TableInstantColumnModel;
import to.remove.uimanager.DateTimeField;
import to.remove.uimanager.PropertySheet;


public class ObsoleteClientUiManager {
    protected final ControllerProxy<? extends ManagedUiModel> controllerProxy;
    protected final PostProcessor postProcessor;
    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();
    private final WeakHashMap<String, Node> idToNodeMap = new WeakHashMap<>(); // TODO: String ist hier "weak" -> Problem!
    private final SimpleObjectProperty<Node> root = new SimpleObjectProperty<>();
    private final Function<String, Node> customComponentSupplier;
    private DolphinEventHandler handler;
    private ChangeListener<Boolean> listener;

    public ObsoleteClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this(controllerProxy, null, null, null);
    }

    public ObsoleteClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy, final DolphinEventHandler handler, PostProcessor postProcessor, Function<String, Node> customComponentSupplier) {
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

        final ManagedUiModel model = controllerProxy.getModel();
        model.rootProperty().onChanged(evt -> createRoot(evt.getNewValue()));
        createRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
            final ItemModel oldDialog = event.getOldValue();
            final ItemModel newDialog = event.getNewValue();
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

    private void createCustomDialog(final CustomDialogModel newDialog) {
        final Dialog<Boolean> dialog = new Dialog<>();
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
        final Optional<Node> nodeOptional = Optional.ofNullable(newDialog.getOwner()).map(modelToNodeMap::get);
        final Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(dialog::initOwner);
        dialog.setTitle(newDialog.getTitle());
        dialog.setHeaderText(newDialog.getHeaderText());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Node content = createNode(newDialog.getContent());
        final DecorationPane decorationPane = new DecorationPane();
        decorationPane.setRoot(content);
        dialog.getDialogPane().setContent(decorationPane);
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
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

    private void createSaveFileDialog(final SaveFileDialogModel newDialog) {
        final FileChooser chooser = new FileChooser();
        if (newDialog.getDirectory() != null) {
            final File folder = new File(newDialog.getDirectory());
            if (!convertFolder(folder).isEmpty()) {
                chooser.setInitialDirectory(folder);
            }
        }
        chooser.setInitialFileName(newDialog.getFileName());
        final File saveFile = chooser.showSaveDialog(findWindowOptional(newDialog).orElse(null));
        if (saveFile != null && !saveFile.isDirectory()) {
            try {
                FileUtils.writeByteArrayToFile(saveFile, newDialog.getSaveThis().getContent());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createUnexpectedErrorDialog(final UnexpectedErrorDialogModel model) {
        final UnexpectedErrorDialog dialog = new UnexpectedErrorDialog();
        configureDialog(dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        dialog.showAndWait();
    }

    private static String convertFolder(final File folder) {
        if (folder != null) {
            if (folder.isDirectory()) {
                try {
                    return folder.getCanonicalPath();
                } catch (final IOException e) {
                    return "";
                }
            } else {
                return "";
            }
        }
        return "";
    }

    private void createConfirmationDialog(final ConfirmationDialogModel newDialog) {
        Objects.requireNonNull(newDialog.getOkayAction());
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        final Optional<Node> nodeOptional = configureDialog(alert, newDialog);
        alert.setHeaderText(newDialog.getHeaderText());
        alert.setContentText(newDialog.getContentText());
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                controllerProxy.invoke(newDialog.getOkayAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(nodeOptional.orElse(null), throwable));
            }
        });
    }

    private void createInfoDialog(final InfoDialogModel newDialog) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        configureDialog(alert, newDialog);
        alert.setHeaderText(newDialog.getHeaderText());
        alert.setContentText(newDialog.getContentText());
        alert.showAndWait();
    }

    private Optional<Node> configureDialog(final Alert alert, final DialogModel model) {
        final Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(modelToNodeMap::get);
        final Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(alert::initOwner);
        alert.setTitle(model.getTitle());
        return nodeOptional;
    }

    void setHandler(final DolphinEventHandler handler) {
        this.handler = handler;
    }

    private void transferFocus(final ItemModel focusThis) {
        final Node node = modelToNodeMap.get(focusThis);
        if (node != null) {
            node.requestFocus();
        }
    }

    private void createRoot(final ItemModel itemModel) {
        root.set(createNode(itemModel));
    }

    public Node createNode(final ItemModel itemModel) {
        try {
            if (itemModel != null && itemModel.getId() != null && idToNodeMap.containsKey(itemModel.getId())) {
                return idToNodeMap.get(itemModel.getId());
            }
            final Node newNode;
//            if (itemModel instanceof TabPaneModel) {
//                newNode = new TabPaneFactory().create()
//            } else
                if (itemModel instanceof PasswordFieldModel) {
                newNode = createPasswordField((PasswordFieldModel) itemModel);
            } else if (itemModel instanceof MigPaneModel) {
                newNode = createMigPane((MigPaneModel) itemModel);
            } else if (itemModel instanceof SegmentedButtonModel) {
                newNode = createSegmentedButton((SegmentedButtonModel) itemModel);
            } else if (itemModel instanceof SplitMenuButtonModel) {
                newNode = createSplitMenuButton((SplitMenuButtonModel) itemModel);
            } else if (itemModel instanceof NotificationPaneModel) {
                newNode = createNotificationPane((NotificationPaneModel) itemModel);
            } else if (itemModel instanceof CustomComponentModel) {
                newNode = createCustomComponent((CustomComponentModel) itemModel);
            }  else if (itemModel instanceof ListViewModel) {
                newNode = createListView((ListViewModel) itemModel);
            } else if (itemModel instanceof HiddenSidesPaneModel) {
                newNode = createHiddenSidesPane((HiddenSidesPaneModel) itemModel);
            } else if (itemModel instanceof UploadButtonModel) {
                newNode = createUploadButton((UploadButtonModel) itemModel);
            } else if (itemModel instanceof FlowPaneModel) {
                newNode = createFlowPane((FlowPaneModel) itemModel);
            } else if (itemModel instanceof SeparatorModel) {
                newNode = createSeparator((SeparatorModel) itemModel);
            } else if (itemModel instanceof TableModel) {
                newNode = createTable((TableModel) itemModel);
            } else if (itemModel instanceof ScrollPaneModel) {
                newNode = createScrollPane((ScrollPaneModel) itemModel);
            } else if (itemModel instanceof ChoiceBoxModel) {
                newNode = createChoiceBox((ChoiceBoxModel) itemModel);
            } else if (itemModel instanceof DateTimeFieldModel) {
                newNode = createDateField((DateTimeFieldModel) itemModel);
            } else if (itemModel instanceof MessagePlaceholder) {
                newNode = createMessagePlaceholder((MessagePlaceholder) itemModel);
            } else if (itemModel instanceof HyperlinkModel) {
                newNode = createHyperlink((HyperlinkModel) itemModel);
            } else if (itemModel instanceof ImageViewModel) {
                newNode = createImageView((ImageViewModel) itemModel);
            } else if (itemModel instanceof TextAreaModel) {
                newNode = createTextArea((TextAreaModel) itemModel);
            } else if (itemModel instanceof PropertySheetModel) {
                newNode = createPropertySheet((PropertySheetModel) itemModel);
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
                final String oldId = findInMap(newNode);
                final String newId = evt.getNewValue();
                idToNodeMap.put(newId, newNode);
                idToNodeMap.remove(oldId);
                postProcessor.postProcess(newId, itemModel, newNode);
            });
            postProcessor.postProcess(itemModel.getId(), itemModel, newNode);
            if (itemModel instanceof WithPadding && newNode instanceof Region) {
                final WithPadding withPadding = (WithPadding) itemModel;
                final Region region = (Region) newNode;
                bind(region.paddingProperty()).to(withPadding.paddingProperty(), value -> value == null ? new Insets(0) : new Insets(withPadding.getPadding()));
            }
            return newNode;
        } catch (final Throwable t) {
            // TODO: Beschissener Workaround, weil exceptions hier sonst nirgends gelogged werden:
            // http://atlassian.sprouts.private:8080/browse/FLY-258
            t.printStackTrace();
            throw t;
        }
    }

    private Node createTabPane(final TabPaneModel itemModel) {
        final TabPane tabPane = new TabPane();
        // TODO: Tab-Inhalt und Tab-Text sind leider nicht an das jeweilige Model gebunden, sondern nur an den Item
        FXBinder.bind(tabPane.getTabs()).to(itemModel.getItems(), model -> {
            final Tab tab = new Tab(model.getCaption(), createNode(model.getContent()));
            tab.setClosable(false);
            return tab;
        });
        FXBinder.bind(tabPane.sideProperty()).to(itemModel.sideProperty(), side -> side == null ? Side.TOP : side);
        return tabPane;
    }

    private Node createMigPane(final MigPaneModel item) {
        final MigPane migPane = new MigPane(item.getLayoutConstraints(), item.getColumnConstraints(), item.getRowConstraints());
        item.getItems().forEach(migPaneItemModel -> {
            final Node child = createNode(migPaneItemModel.getItem());
            migPane.add(child, migPaneItemModel.getConstraints());
        });
        item.getItems().onChanged(evt -> {
            migPane.getChildren().clear();
            item.getItems().forEach(migPaneItemModel -> {
                final Node child = createNode(migPaneItemModel.getItem());
                migPane.add(child, migPaneItemModel.getConstraints());
            });
        });
        return migPane;
    }

    private Node createSegmentedButton(final SegmentedButtonModel itemModel) {
        final SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null && oldVal != null)
                oldVal.setSelected(true);
        });
        FXBinder.bind(segmentedButton.getButtons()).to(itemModel.getItems(), model -> (ToggleButton) createNode(model));
        return segmentedButton;
    }

    private void createQualifiedErrorDialog(final QualifiedErrorDialogModel model) {
        final QualifiedErrorDialog dialog = new QualifiedErrorDialog();
        configureDialog(dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        bind(dialog.headerTextProperty()).to(model.headerTextProperty());
        dialog.setContentText(model.getContentText());
        dialog.setRootCauseText(model.getRootCauseText());
        dialog.showAndWait();
    }

    private Optional<Window> findWindowOptional(final DialogModel newDialog) {
        final Optional<Node> nodeOptional = Optional.ofNullable(newDialog.getOwner()).map(modelToNodeMap::get);
        return nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
    }


    private Node createCustomComponent(final CustomComponentModel itemModel) {
        return customComponentSupplier.apply(itemModel.getType());
    }

    private Node createSplitMenuButton(final SplitMenuButtonModel itemModel) {
        final SplitMenuButton button = new SplitMenuButton();
        button.setOnAction(event -> controllerProxy.invoke(itemModel.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(button.textProperty()).to(itemModel.captionProperty());
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionAProperty(), itemModel.captionAProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionBProperty(), itemModel.captionBProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionCProperty(), itemModel.captionCProperty(), button));
        button.getItems().add(createSpliteMenuMenuItem(itemModel.actionDProperty(), itemModel.captionDProperty(), button));
        return button;
    }

    private MenuItem createSpliteMenuMenuItem(final Property<String> actionProperty, final Property<String> captionProperty, final SplitMenuButton button) {
        final MenuItem menuItem = new MenuItem();
        menuItem.setVisible(actionProperty.get() != null);
        actionProperty.onChanged(evt -> menuItem.setVisible(evt.getNewValue() != null));
        menuItem.setOnAction(event -> controllerProxy.invoke(actionProperty.get()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable)));
        bind(menuItem.textProperty()).to(captionProperty);
        return menuItem;
    }

    private void createShutdownDialog(final ShutdownDialogModel model) {
        getRoot().getScene().getWindow().hide();
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (model.getOwner() != null) {
            alert.initOwner(modelToNodeMap.get(model.getOwner()).getScene().getWindow());
        }
        alert.setTitle("Neustart der Software");
        alert.setHeaderText("sprouts fly office erhält gerade eine Aktualisierung.");
        alert.setContentText("Die Anwendung hat sich deshalb automatisch beendet.");
        alert.showAndWait();
        System.exit(0);
    }

    protected EventHandler<ActionEvent> createOnActionHandler(final IdentifiableModel identifiableModel) {
        return createOnActionHandler("buttonClick", identifiableModel);
    }

    private EventHandler<ActionEvent> createOnActionHandler(final String type, final IdentifiableModel identifiableModel) {
        return event -> {
            event.consume();
            if (identifiableModel instanceof ButtonModel && ((ButtonModel) identifiableModel).getAction() != null) {
                final String action = ((ButtonModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else if (identifiableModel instanceof ToggleItemModel && ((ToggleItemModel) identifiableModel).getAction() != null) {
                final String action = ((ToggleItemModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else {
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Node createListView(final ListViewModel itemModel) {
        final ListView<ListViewItemModel> listView = new ListView<>();
        bind(listView.getItems()).to(itemModel.getItems());
        ClientUiHelper.bindWithSelectionModel(itemModel.selectedProperty(), listView.getSelectionModel());
        if (itemModel.getSelectedAction() != null) {
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> controllerProxy.invoke(itemModel.getSelectedAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(listView, throwable)));
        }
        bind(listView.cellFactoryProperty()).to(itemModel.rendererClassProperty(), className -> {
            if (className == null) return null;
            return view -> {
                try {
                    final ListCellSkin<ListViewItemModel> cellSkin = (ListCellSkin<ListViewItemModel>)
                            Class.forName(className).getConstructor().newInstance();
                    cellSkin.setOwner(itemModel);
                    cellSkin.setControllerProxy(controllerProxy);
                    return new EditableListCell<>(cellSkin);
                } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new IllegalArgumentException(e);
                }
            };
        });
        itemModel.rendererClassProperty().onChanged(evt -> listView.setCellFactory(view -> new EditableListCell<>(new DocumentTemplateListCellSkin())));
        return listView;
    }

    private Node createHiddenSidesPane(final HiddenSidesPaneModel itemModel) {
        final Callback<ItemModel, Node> convert = param -> {
            if (param == null) {
                return null;
            }
            return createNode(param);
        };
        final HiddenSidesPane pane = new HiddenSidesPane();
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


    private Node createUploadButton(final UploadButtonModel itemModel) {
        final ButtonBase button = configureButton(itemModel, new Button());
        button.setOnAction(event -> onDoUpload(event, itemModel));
        return button;
    }

    private void onDoUpload(final ActionEvent actionEvent, final UploadButtonModel uploadButton) {
        Assert.requireNonNull(uploadButton.getUploadUrl(), "buttonModel.uploadUrl");
        final Button sourceButton = (Button) actionEvent.getSource();
        final FileChooser chooser = new FileChooser();
        final File loadFile = chooser.showOpenDialog(sourceButton.getScene().getWindow());
        if (loadFile != null && loadFile.exists() && loadFile.isFile() && !loadFile.isDirectory()) {
            try {
                final DocumentData documentData = DocumentData.from(loadFile);
                final Object detectedMimeType = documentData.getMimeType();
                if (detectedMimeType == null || uploadButton.getAllowedMimeTypes().stream()
                        .noneMatch(mimeType -> mimeType.equals(detectedMimeType.toString()))) {
                    final Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
                    final HttpClient httpClient = Client.getService(HttpClient.class);
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
            } catch (final IOException exception) {
                exception.printStackTrace();
                throw new IllegalArgumentException(exception);
            }
        }
    }

    private void doRemoteCall(final String remoteAction) {
        Objects.requireNonNull(remoteAction);
        controllerProxy.invoke(remoteAction)
                .exceptionally(throwable -> UnexpectedErrorDialog.showError(getRoot(), throwable));
    }

    private Node createFlowPane(final FlowPaneModel model) {
        final FlowPane pane = new FlowPane();
        bind(pane.vgapProperty()).to(model.vGapProperty());
        bind(pane.hgapProperty()).to(model.hGapProperty());
        bind(pane.orientationProperty()).to(model.orientationProperty());
        bind(pane.alignmentProperty()).to(model.alignmentProperty());
        bind(pane.getChildren()).to(model.getItems(), content -> createNode(content.getItem()));
        return pane;
    }

    private Node createSeparator(final SeparatorModel itemModel) {
        final Separator separator = new Separator();
        bind(separator.orientationProperty()).to(itemModel.orientationProperty());
        return separator;
    }

    private Node createTable(final TableModel model) {
        final TableView<TableRowModel> table = new TableView<>();
        bind(table.editableProperty()).to(model.editableProperty(), value -> value == null ? false : value);
        new IndexedJavaFXListBinder<>(table.getColumns()).bidirectionalTo(model.getColumns(), conversionInfo -> createTableColumn(table, conversionInfo), conversionInfo -> (TableColumnModel) conversionInfo.getInput().getUserData());

        // bidirektional, damit Tabellensortierung funktioniert
        bind(table.getItems()).bidirectionalTo(model.getRows());
        ClientUiHelper.bindWithSelectionModel(model.selectedRowProperty(), table.getSelectionModel());

        FXBinder.bind(table.placeholderProperty()).to(model.placeholderProperty(),
                itemModel -> itemModel == null ? new Label("Nichts gefunden.") : createNode(itemModel));

        return table;
    }

    private TableColumn<TableRowModel, ?> createTableColumn(final TableView<TableRowModel> table, final IndexedJavaFXListBinder.ConversionInfo<TableColumnModel> conversionInfo) {
        final TableColumn<TableRowModel, Object> tableColumn = new TableColumn<>(conversionInfo.getInput().getCaption());

        new IndexedJavaFXListBinder<>(tableColumn.getColumns()).bidirectionalTo(conversionInfo.getInput().getChildren(), subConversionInfo -> createTableColumn(table, subConversionInfo), subConversionInfo -> (TableColumnModel) subConversionInfo.getInput().getUserData());

        table.setUserData(conversionInfo.getInput());
        bind(tableColumn.textProperty()).to(conversionInfo.getInput().captionProperty());
        tableColumn.setCellValueFactory(param -> FXWrapper.wrapObjectProperty(
                param.getValue().getCells().get(conversionInfo.getIndex()).valueProperty()));
        bind(tableColumn.prefWidthProperty()).to(conversionInfo.getInput().prefWidthProperty(), value -> value == null ? 80 : value);
        bind(tableColumn.editableProperty()).to(conversionInfo.getInput().editableProperty(), value -> value == null ? true : value);
        if (conversionInfo.getInput() instanceof TableInstantColumnModel) {
            final TableInstantColumnModel columnModel = (TableInstantColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(column -> new TableCell<TableRowModel, Object>() {
                protected void updateItem(final Object item, final boolean empty) {
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
                        final Object newValue = t.getNewValue();
                        controllerProxy.invoke("onTableStringCommit",
                                new Param("row", t.getRowValue().getReference()),
                                new Param("column", conversionInfo.getInput().getReference()),
                                new Param("newValue", newValue)).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable));
                    }
            );
        } else if (conversionInfo.getInput() instanceof TableChoiceBoxColumnModel) {
            final TableChoiceBoxColumnModel choiceBoxColumnModel = (TableChoiceBoxColumnModel) conversionInfo.getInput();
            tableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(new ChoiceBoxItemConverter(choiceBoxColumnModel.getItems()), (javafx.collections.ObservableList) FXWrapper.wrapList(choiceBoxColumnModel.getItems())));
            tableColumn.setOnEditCommit(t -> {
                        final Object newValue = t.getNewValue();
                        controllerProxy.invoke("onTableChoiceBoxCommit",
                                new Param("row", t.getRowValue().getReference()),
                                new Param("column", conversionInfo.getInput().getReference()),
                                new Param("newValue", ((ChoiceBoxItemModel) newValue).getReference())).exceptionally(throwable -> UnexpectedErrorDialog.showError(table, throwable));
                    }
            );
        } else if (conversionInfo.getInput() instanceof TableCheckBoxColumnModel) {
            tableColumn.setCellValueFactory(param -> {
                final TableRowModel row = param.getValue();
                final BooleanProperty property = new SimpleBooleanProperty((Boolean) row.getCells().get(conversionInfo.getIndex()).getValue());
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


    protected MenuItem createMenuItem(final MenuButtonItemModel model) {
        final MenuItem menuItem = new MenuItem();
        bind(menuItem.textProperty()).to(model.captionProperty());
        if (model.getAction() != null) {
            menuItem.setOnAction(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(null, throwable)));
        } else {
            menuItem.setOnAction(createOnActionHandler(model));
        }
        return menuItem;
    }

    private Node createNotificationPane(final NotificationPaneModel itemModel) {
        final NotificationPane pane = new NotificationPane();
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

    private Node createScrollPane(final ScrollPaneModel itemModel) {
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        bind(scrollPane.contentProperty()).to(itemModel.contentProperty(), this::createNode);
        return scrollPane;
    }

    private Node createChoiceBox(final ChoiceBoxModel itemModel) {
        return new ManagedChoiceBox(itemModel, controllerProxy);
    }


    private Node createDateField(final DateTimeFieldModel itemModel) {
        return new DateTimeField(itemModel);
    }

    private Node createMessagePlaceholder(final MessagePlaceholder itemModel) {
        final Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setTextFill(Color.RED);
        label.setPadding(new Insets(0, 0, 0, 10));
        label.setWrapText(true);
        return label;
    }

    private Node createHyperlink(final HyperlinkModel itemModel) {
        final ButtonBase button = configureButton(itemModel, new Hyperlink());
        installMonitoredAction(button, createOnActionHandler(itemModel));
        return button;
    }

    private Node createImageView(final ImageViewModel itemModel) {
        final ImageView imageView = new ImageView();
        bind(imageView.imageProperty()).to(itemModel.resourcePathProperty(), value -> value == null ? null : new javafx.scene.image.Image(
                Image.class.getResource(value.substring("classpath:".length())).toExternalForm()));
        bind(imageView.preserveRatioProperty()).to(itemModel.preserveRatioProperty(), value -> value == null ? true : value);
        bind(imageView.fitWidthProperty()).to(itemModel.fitWidthProperty());
        bind(imageView.fitHeightProperty()).to(itemModel.fitHeightProperty());
        return imageView;
    }

    protected void loadDocumentFromServerAndShow(final String documentId, final Consumer<DocumentData> onFinish) {
        AsyncSequence.doAsync(() -> {
            try {
                if (documentId == null) {
                    return null;
                }
                final HttpClient httpClient = Client.getService(HttpClient.class);
                return DocumentData.from(httpClient.request(Configuration.getServerUrl() + "/api/document/get/" + documentId, RequestMethod.GET)
                        .withoutContent().readBytes()
                        .execute().get().getContent().get());
            } catch (final InterruptedException | ExecutionException e) {
                return null;
            }
        }, onFinish, exception -> {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Dokument nicht verfügbar.");
            alert.setContentText("Das System konnte das Dokument nicht abrufen.");
            alert.showAndWait();
        });
    }

    private Node createSplitPane(final SplitPaneModel splitPaneModel) {
        final SplitPane splitPane = new SplitPane();
        new IndexedJavaFXListBinder<>(splitPane.getItems()).to(splitPaneModel.getItems(), info -> createDividerContent(splitPane, info));
        bind(splitPane.orientationProperty()).to(splitPaneModel.orientationProperty());
        return splitPane;
    }

    private Node createDividerContent(final SplitPane
                                              splitPane, final IndexedJavaFXListBinder.ConversionInfo<SplitPaneItemModel> info) {
        final Node newNode = createNode(info.getInput().getContent());
        splitPane.setDividerPosition(info.getIndex(), info.getInput().getDividerPosition());
        info.getInput().contentProperty().onChanged(evt -> splitPane.getItems().set(info.getIndex(), createNode(evt.getNewValue())));
        return newNode;
    }

    private Node createToolBar(final ToolBarModel itemModel) {
        final ToolBar toolBar = new ToolBar();
        updateBindItems(itemModel.getItems(), toolBar.getItems());
        return toolBar;
    }

    private void updateBindItems(final ObservableList<ItemModel> remoteItems, final javafx.collections.ObservableList<Node> fxItems) {
        bind(fxItems).to(remoteItems, this::createNode);
    }

    private Node createBorderPane(final BorderPaneModel itemModel) {
        final Callback<ItemModel, Node> convert = param -> {
            if (param == null) {
                return null;
            }
            return createNode(param);
        };
        final BorderPane pane = new BorderPane();
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

    private Node createPropertySheet(final PropertySheetModel model) {
        return new PropertySheet(controllerProxy, model);
    }

    private void configureNode(final Node configureThis, final ItemModel from) {
        bind(configureThis.disableProperty()).to(from.disableProperty());
        bind(configureThis.visibleProperty()).to(from.visibleProperty());
        bind(configureThis.managedProperty()).to(from.managedProperty());
        from.getStyleClass().addAll(configureThis.getStyleClass());
        bind(configureThis.styleProperty()).to(from.styleProperty());
        bind(configureThis.getStyleClass()).bidirectionalTo(from.getStyleClass());
        if (configureThis instanceof Region) {
            final Region region = (Region) configureThis;
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
            } catch (final IllegalStateException e) {
                // Gerät sonst in eine Endlosfehlerschleife...
                configureThis.focusedProperty().removeListener(listener);
            }
        };
        configureThis.focusedProperty().addListener(listener);
    }

    private void updateValidationFeedback(final Node node, final ItemModel itemModel) {
        Decorator.removeAllDecorations(node);
        // TODO:  Überschreibt individuell im Server gesetzte tooltips
//node.setTooltip(null);
        final Label display = (Label) modelToNodeMap.get(itemModel.getMessageDisplay());
        if (display != null) {
            display.setManaged(false);
            display.setVisible(false);
        }
        final ObservableList<String> validationMessages = itemModel.getValidationMessages();
        if (!validationMessages.isEmpty()) {
            Decorator.addDecoration(node, new StyleClassDecoration("warning"));
            final List<Node> children = ImplUtils.getChildren(node.getParent(), true);
            final StringBuilder text = new StringBuilder();
            for (final String constraintViolation : validationMessages) {
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

    private String findInMap(final Node node) {
        requireNonNull(node);
        for (final String id : idToNodeMap.keySet()) {
            if (idToNodeMap.get(id) == node) {
                return id;
            }
        }
        throw new IllegalArgumentException("Expected node '" + node + "' in idToNodeMap");
    }

    private Node createGridPane(final GridPaneModel item) {
        final GridPane gridPane = new GridPane();
//        Platform.runLater(() -> gridPane.setGridLinesVisible(true));
        bind(gridPane.hgapProperty()).to(item.hGapProperty());
        bind(gridPane.vgapProperty()).to(item.vGapProperty());
        bind(gridPane.getChildren()).to(item.getItems(), content -> {
            final Node child = createNode(content.getItem());
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

    private Node createToggleItem(final ToggleItemModel item) {
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

    private Node createRadioButton(final RadioButtonModel item) {
        final RadioButton button = new RadioButton();
        bind(button.selectedProperty()).bidirectionalTo(item.selectedProperty());
        bind(button.textProperty()).to(item.captionProperty());
        button.setOnAction(createOnActionHandler(item));
        return button;
    }

    private ToggleButton createToggleButton(final ToggleButtonModel item) {
        final ToggleButton button = new ToggleButton();
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

    private Node createCheckBox(final CheckBoxModel item) {
        final CheckBox checkBox = new CheckBox();
        bind(checkBox.selectedProperty()).bidirectionalTo(item.selectedProperty());
        bind(checkBox.textProperty()).to(item.captionProperty());
        checkBox.setOnAction(createOnActionHandler(item));
        return checkBox;
    }

    private Node createSingleItemContainer(final SingleItemContainerModel container) {
        if (container instanceof TitledPaneModel) {
            return createTitledPane((TitledPaneModel) container);
        } else {
            throw new IllegalArgumentException("Unknown container type: " + container);
        }
    }

    private Node createTitledPane(final TitledPaneModel pane) {
        final TitledPane result = new TitledPane();
        bind(result.textProperty()).to(pane.titleProperty());
        bind(result.contentProperty()).to(pane.contentProperty(), this::createNode);
        return result;
    }

    private Node createButton(final ButtonModel item) {
        final ButtonBase button = configureButton(item, new Button());
        installMonitoredAction(button, createOnActionHandler(item));
        return button;
    }

    protected ButtonBase configureButton(final ButtonModel item, final ButtonBase button) {
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
                final String imagePath = "/image/" + optionalImagePath.get();
                final URL resource = ObsoleteClientUiManager.class.getResource(imagePath);
                requireNonNull(resource, "Could not find classpath resource '" + imagePath + "'");
                graphic = new ImageView(new javafx.scene.image.Image(resource.toExternalForm()));
                graphic.setPreserveRatio(true);
                FXBinder.bind(graphic.fitHeightProperty()).to(item.prefHeightProperty());
            }
            button.setGraphic(graphic);
        });
        return button;
    }

    private void installMonitoredAction(final ButtonBase button, final EventHandler<ActionEvent> onActionHandler) {
        final List<EventHandler<ActionEvent>> handlers = new LinkedList<>();
        final EventHandler<ActionEvent> actionEventEventHandler = event -> handlers.forEach(handler -> handler.handle(event));
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

    private Node createPasswordField(final PasswordFieldModel item) {
        final PasswordField result = new PasswordField();
        bind(result.prefColumnCountProperty()).to(item.prefColumnCountProperty());
        configureTextInputControl(controllerProxy, item, result);
        return result;
    }

    private Node createTextField(final TextFieldModel item) {
        return new TextField(controllerProxy, item);
    }

    private Node createTextArea(final TextAreaModel model) {
        return new TextArea(controllerProxy, model);
    }


    private Node createListItemContainer(final ItemListContainerModel item) {
        final Pane pane;
        if (item instanceof HBoxModel) {
            pane = createHBox((HBoxModel) item);
        } else if (item instanceof VBoxModel) {
            pane = createVBox((VBoxModel) item);
        } else {
            throw new IllegalArgumentException("Unknown container type: " + item);
        }
        return pane;
    }

    private VBox createVBox(final VBoxModel item) {
        final VBox vBox = new VBox();
        bind(vBox.spacingProperty()).to(item.spacingProperty(), value -> value == null ? 0 : value);
        bind(vBox.alignmentProperty()).to(item.alignmentProperty());
        bind(vBox.getChildren()).to(item.getItems(), content -> {
            final Node child = createNode(content.getItem());
            VBox.setVgrow(child, content.getvGrow());
            return child;
        });
        return vBox;
    }

    private HBox createHBox(final HBoxModel item) {
        final HBox hBox = new HBox();
        bind(hBox.spacingProperty()).to(item.spacingProperty(), value -> value == null ? 0 : value);
        bind(hBox.alignmentProperty()).to(item.alignmentProperty());
        bind(hBox.getChildren()).to(item.getItems(), content -> {
            final Node child = createNode(content.getItem());
            HBox.setHgrow(child, content.gethGrow());
            return child;
        });
        return hBox;
    }

    private Node createLabel(final LabelModel labelModel) {
        final Label result = new Label(labelModel.getText());
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
    public <T extends Node> T getNodeById(final String id) {
        return requireNonNull((T) idToNodeMap.get(id), "Missing injected node with id: " + id);
    }

    private static class PlainStringConverter extends StringConverter<Object> {
        @Override
        public String toString(final Object object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public Object fromString(final String string) {
            return string;
        }
    }

    private static class BooleanConverter extends StringConverter<Object> {
        @Override
        public String toString(final Object object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public Object fromString(final String string) {
            return Boolean.valueOf(string);
        }
    }

    private static class ChoiceBoxItemConverter extends StringConverter<Object> {
        private final ObservableList<ChoiceBoxItemModel> items;

        ChoiceBoxItemConverter(final ObservableList<ChoiceBoxItemModel> items) {
            this.items = items;
        }

        @Override
        public String toString(final Object object) {
            if (object == null) return null;
            return ((ChoiceBoxItemModel) object).getCaption();
        }

        @Override
        public Object fromString(final String caption) {
            return items.stream().filter(choiceBoxItemModel -> choiceBoxItemModel.getCaption().equals(caption)).findFirst().orElse(null);
        }
    }

    private class ActionEventEventHandler implements EventHandler<ActionEvent> {
        private final CustomDialogModel newDialog;
        private final Button okButton;
        private boolean invokeAction = true;
        private final Object lock = new Object();

        ActionEventEventHandler(final CustomDialogModel newDialog, final Button okButton) {
            this.newDialog = newDialog;
            this.okButton = okButton;
        }

        @Override
        public void handle(final ActionEvent event) {
            synchronized (lock) {
                if (invokeAction) {
                    invokeAction = false;
                    event.consume();
                    final Thread thread = new Thread(() -> {
                        final CompletableFuture<Void> future = controllerProxy.invoke(newDialog.getCheckAction());
                        try {
                            future.get();
                        } catch (final InterruptedException | ExecutionException e) {
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
