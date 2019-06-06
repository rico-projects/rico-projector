package to.remove;

import static dev.rico.client.remoting.FXBinder.bind;
import static dev.rico.client.remoting.FXWrapper.wrapStringProperty;
import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;

import java.util.function.Function;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.core.functional.Binding;
import dev.rico.internal.client.projector.uimanager.ObsoleteClientUiManager;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import to.remove.ui.DocumentViewModel;
import to.remove.ui.FuelFieldModel;
import to.remove.ui.PaxCodeFieldModel;
import to.remove.ui.ProgressIndicatorModel;
import to.remove.ui.autocompletion.AutoCompleteModel;
import to.remove.ui.breadcrumbbar.BreadCrumbBarModel;
import to.remove.ui.cardpane.CardPaneItemModel;
import to.remove.ui.cardpane.CardPaneModel;
import to.remove.ui.checklistview.CheckListViewModel;
import to.remove.ui.listselectionview.ListSelectionViewModel;
import to.remove.ui.menubutton.MenuButtonModel;
import to.remove.ui.nestedmenubutton.NestedMenuButtonModel;
import to.remove.uimanager.CardPane;
import to.remove.uimanager.CheckListView;
import to.remove.uimanager.FuelField;
import to.remove.uimanager.MenuButton;
import to.remove.uimanager.PaxCodeField;
import to.remove.uimanager.ProgressIndicator;
import to.remove.uimanager.ServerBackedAutoCompletionField;

public class SproutsClientUiManager extends ObsoleteClientUiManager {
    public SproutsClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        super(controllerProxy);
    }

    public SproutsClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy, final DolphinEventHandler handler, final PostProcessor postProcessor, final Function<String, Node> customComponentSupplier) {
        super(controllerProxy, handler, postProcessor, customComponentSupplier);
    }

    @Override
    public Node createNode(final ItemModel itemModel) {
        Node newNode = null;
        if (itemModel instanceof CheckListViewModel) {
            newNode = createCheckListView((CheckListViewModel) itemModel);
        } else if (itemModel instanceof CardPaneModel) {
            newNode = createCardPane((CardPaneModel) itemModel);
        } else if (itemModel instanceof ListSelectionViewModel) {
//            newNode = createListSelectionView((ListSelectionViewModel) itemModel);
        } else if (itemModel instanceof NestedMenuButtonModel) {
            newNode = createNestedMenuButton((NestedMenuButtonModel) itemModel);
        } else if (itemModel instanceof BreadCrumbBarModel) {
//            newNode = createBreadCrumbBar((BreadCrumbBarModel) itemModel);
        } else if (itemModel instanceof FuelFieldModel) {
            newNode = createFuelFieldModel((FuelFieldModel) itemModel);
        } else if (itemModel instanceof PaxCodeFieldModel) {
            newNode = createPaxCodeFieldModel((PaxCodeFieldModel) itemModel);
        } else if (itemModel instanceof MenuButtonModel) {
            newNode = createMenuButton((MenuButtonModel) itemModel);
        } else if (itemModel instanceof AutoCompleteModel) {
            newNode = createAutoCompleteField((AutoCompleteModel) itemModel);
        } else if (itemModel instanceof ProgressIndicatorModel) {
            newNode = createProgressIndicator((ProgressIndicatorModel) itemModel);
        } else if (itemModel instanceof DocumentViewModel) {
            newNode = createDocumentView((DocumentViewModel) itemModel);
        } else
            newNode = super.createNode(itemModel);
        if (newNode instanceof to.remove.uimanager.breadcrumbbar.BreadCrumbBar) {
            final ScrollPane scrollPane = new ScrollPane(new Group(newNode));
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setFitToHeight(true);
            scrollPane.setPannable(true);
            return scrollPane;
        }
        return newNode;
    }

    private Node createCheckListView(final CheckListViewModel itemModel) {
        return new CheckListView(itemModel);
    }

    private Node createNestedMenuButton(final NestedMenuButtonModel itemModel) {
        final ButtonBase button = configureButton(itemModel, new MenuButton());
        bind(button.textProperty()).to(itemModel.captionProperty());
        bind(((MenuButton) button).getItems()).to(itemModel.getItems(), this::createNestedMenuItem);
        return button;
    }

    private MenuItem createNestedMenuItem(final MenuItemModel model) {
        if (model.getItems().isEmpty()) {
            final MenuItem menuItem = new MenuItem();
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
            final Menu menu = new Menu();
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

    private Node createCardPane(final CardPaneModel model) {
        final CardPane cardPane = new CardPane(model);
        bind(cardPane.getChildren()).to(model.getItems(),
                cardPaneItemModel -> createNode(cardPaneItemModel.getItem()));
        model.visibleItemProperty().onChanged(evt -> {
            final CardPaneItemModel visibleOne = evt.getNewValue();
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


    private Node createProgressIndicator(final ProgressIndicatorModel itemModel) {
        final ProgressIndicator progressIndicator = new ProgressIndicator();
        bind(itemModel.waitingProperty()).to(progressIndicator.activeProperty());
        return progressIndicator;
    }

    private Node createDocumentView(final DocumentViewModel itemModel) {
//        DocumentViewer documentViewer = new DocumentViewer(this, itemModel);
//        Consumer<DocumentData> consumer = documentData -> {
//            documentViewer.closeDocument();
//            if (documentData != null) {
//                documentViewer.openDocument(documentData, "");
//            }
//        };
//        consumer.accept(itemModel.getDocumentData());
//        CommonUiHelper.subscribeWithOptional(itemModel.documentByIdProperty(), idOptional -> {
//            documentViewer.closeDocument();
//            idOptional.ifPresent(id -> loadDocumentFromServerAndShow(id, consumer));
//        });
//        itemModel.documentDataProperty().onChanged(evt -> consumer.accept(evt.getNewValue()));
//        return documentViewer;
        return new Label("Placeholder");
    }

    private Node createFuelFieldModel(final FuelFieldModel model) {
        final FuelField field = new FuelField(model);
        final Binding textPropertyBinding = configureTextInputControl(controllerProxy, model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    private Node createPaxCodeFieldModel(final PaxCodeFieldModel model) {
        final PaxCodeField field = new PaxCodeField();
        final Binding textPropertyBinding = configureTextInputControl(controllerProxy, model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    private Node createMenuButton(final MenuButtonModel itemModel) {
        final MenuButton button = new MenuButton();
        if (itemModel.getCaption() == null) {
            button.getStyleClass().add("activeButton");
            button.setGraphic(new ImageView(Image.COG));
        } else {
            bind(button.textProperty()).to(itemModel.captionProperty());
        }
        bind(button.getItems()).to(itemModel.getItems(), this::createMenuItem);
        return button;
    }


    private Node createAutoCompleteField(final AutoCompleteModel itemModel) {
        return new ServerBackedAutoCompletionField(controllerProxy, itemModel);
    }

}
