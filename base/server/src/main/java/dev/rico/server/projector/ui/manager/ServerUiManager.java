package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.mixed.CommonUiHelper;
import dev.rico.common.projector.mixed.DocumentData;
import dev.rico.common.projector.mixed.RemotingEvent;
import dev.rico.common.projector.ui.*;
import dev.rico.common.projector.ui.autocompletion.AutoCompleteItemModel;
import dev.rico.common.projector.ui.autocompletion.AutoCompleteModel;
import dev.rico.common.projector.ui.box.HBoxItemModel;
import dev.rico.common.projector.ui.box.HBoxModel;
import dev.rico.common.projector.ui.box.VBoxItemModel;
import dev.rico.common.projector.ui.box.VBoxModel;
import dev.rico.common.projector.ui.breadcrumbbar.BreadCrumbBarModel;
import dev.rico.common.projector.ui.breadcrumbbar.BreadCrumbItemModel;
import dev.rico.common.projector.ui.cardpane.CardPaneItemModel;
import dev.rico.common.projector.ui.cardpane.CardPaneModel;
import dev.rico.common.projector.ui.checklistview.CheckListViewModel;
import dev.rico.common.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.common.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.common.projector.ui.dialog.*;
import dev.rico.common.projector.ui.flowpane.FlowPaneItemModel;
import dev.rico.common.projector.ui.flowpane.FlowPaneModel;
import dev.rico.common.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.common.projector.ui.gridpane.GridPaneModel;
import dev.rico.common.projector.ui.listselectionview.ListSelectionViewItemModel;
import dev.rico.common.projector.ui.listselectionview.ListSelectionViewModel;
import dev.rico.common.projector.ui.listview.ListViewItemModel;
import dev.rico.common.projector.ui.listview.ListViewModel;
import dev.rico.common.projector.ui.menubutton.MenuButtonItemModel;
import dev.rico.common.projector.ui.menubutton.MenuButtonModel;
import dev.rico.common.projector.ui.menuitem.MenuItemModel;
import dev.rico.common.projector.ui.migpane.MigPaneItemModel;
import dev.rico.common.projector.ui.migpane.MigPaneModel;
import dev.rico.common.projector.ui.nestedmenubutton.NestedMenuButtonModel;
import dev.rico.common.projector.ui.propertysheet.PropertySheetItemGroupModel;
import dev.rico.common.projector.ui.propertysheet.PropertySheetItemModel;
import dev.rico.common.projector.ui.propertysheet.PropertySheetModel;
import dev.rico.common.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.common.projector.ui.splitpane.SplitPaneModel;
import dev.rico.common.projector.ui.table.*;
import dev.rico.common.projector.ui.tabpane.TabPaneItemModel;
import dev.rico.common.projector.ui.tabpane.TabPaneModel;
import dev.rico.internal.remoting.MockedProperty;
import dev.rico.remoting.BeanManager;
import dev.rico.remoting.Property;
import javafx.geometry.Orientation;
import javafx.scene.layout.Priority;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class ServerUiManager {

    private final ManagedUiModel model;
    private final BeanManager beanManager;
    private final WeakHashMap<String, IdentifiableModel> idToItemMap = new WeakHashMap<>();
    private final WeakHashMap<IdentifiableModel, Runnable> modelToEventHandlerMap = new WeakHashMap<>();

    public ServerUiManager(ManagedUiModel model, BeanManager beanManager) {
        this.model = model;
        this.beanManager = beanManager;
    }



    public VBoxModel vBox() {
        return create(VBoxModel.class);
    }

    public VBoxModel vBox(VBoxItemModel... childs) {
        VBoxModel vBox = vBox();
        vBox.addAll(childs);
        return vBox;
    }

    public <T extends IdentifiableModel> T create(Class<T> beanClass) {
        return create(beanClass, UUID.randomUUID().toString());
    }

    private <T extends IdentifiableModel> T create(Class<T> beanClass, String id) {
        T model = beanManager.create(beanClass);
        model.setId(id);
        if (idToItemMap.containsKey(id)) {
            throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + id + "' is already registered");
        }
        idToItemMap.put(id, model);
        model.idProperty().onChanged(evt -> {
            if (idToItemMap.containsKey(evt.getNewValue())) {
                throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + evt.getNewValue() + "' is already registered");
            }
            String oldId = findInMap(idToItemMap, model);
            idToItemMap.put(evt.getNewValue(), model);
            idToItemMap.remove(oldId);
        });
        if (model instanceof ItemModel) {
            ItemModel itemModel = (ItemModel) model;
            itemModel.setDisable(false);
            itemModel.setManaged(true);
            itemModel.setVisible(true);
        }
        return model;
    }

    private <T> String findInMap(Map<String, T> map, T model) {
        java.util.Objects.requireNonNull(map);
        java.util.Objects.requireNonNull(model);
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (entry.getValue() == model) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Could not find '" + model + "' in map");
    }

    public HBoxModel hBox() {
        return create(HBoxModel.class);
    }

    public LabelModel label() {
        return label(null);
    }

    public LabelModel label(String text) {
        LabelModel labelModel = create(LabelModel.class);
        labelModel.setWrapText(true);
        labelModel.setText(text);
        return labelModel;
    }

    public FuelFieldModel fuelField() {
        FuelFieldModel fuelFieldModel = create(FuelFieldModel.class);
        fuelFieldModel.setPromptText("Min, Max, Remaining");
        return fuelFieldModel;
    }

    public TextFieldModel textField() {
        return create(TextFieldModel.class);
    }

    public MenuButtonModel menuButton() {
        return menuButton(null);
    }

    public MenuButtonModel menuButton(String caption) {
        MenuButtonModel button = create(MenuButtonModel.class);
        button.setCaption(caption);
        return button;
    }

    public MenuButtonItemModel menuButtonItem(String caption) {
        return menuButtonItem(caption, (Runnable) null);
    }

    public MenuButtonItemModel menuButtonItem(String caption, String action) {
        MenuButtonItemModel model = menuButtonItem(caption, (Runnable) null);
        model.setAction(action);
        return model;
    }

    public MenuButtonItemModel menuButtonItem(String caption, Runnable handler) {
        MenuButtonItemModel model = create(MenuButtonItemModel.class);
        model.setCaption(caption);
        maybeInstallActionHandler(model, handler);
        return model;
    }

    // TODO: Auf neues Action-System umstellen
    @Deprecated
    public void maybeInstallActionHandler(IdentifiableModel model, Runnable handler) {
        if (handler != null && model != null) {
            modelToEventHandlerMap.put(model, handler);
        }
    }

    public ButtonModel button(Runnable handler) {
        return button(null, handler);
    }

    public ButtonModel button(String caption, Runnable handler) {
        ButtonModel buttonModel = create(ButtonModel.class);
        return configureButton(buttonModel, caption, handler);
    }

    public ButtonModel button(String caption, String action) {
        ButtonModel button = button(caption, (Runnable) null);
        button.setAction(action);
        return button;
    }

    private ButtonModel configureButton(ButtonModel buttonModel, String caption, Runnable handler) {
        buttonModel.setCaption(caption);
        maybeInstallActionHandler(buttonModel, handler);
        return buttonModel;
    }

    public TitledPaneModel titledPane(String title) {
        TitledPaneModel titledPaneModel = create(TitledPaneModel.class);
        titledPaneModel.setTitle(title);
        return titledPaneModel;
    }

    public GridPaneModel gridPane() {
        return create(GridPaneModel.class);
    }

    public GridPaneItemModel gridPaneContent(int col, int row, ItemModel itemModel) {
        GridPaneItemModel content = create(GridPaneItemModel.class);
        content.setCol(col);
        content.setRow(row);
        content.setItem(itemModel);
        return content;
    }

    public GridPaneItemModel gridPaneContent(int col, int row, int colSpan, int rowSpan, ItemModel itemModel) {
        GridPaneItemModel content = create(GridPaneItemModel.class);
        content.setCol(col);
        content.setRow(row);
        content.setColSpan(colSpan);
        content.setRowSpan(rowSpan);
        content.setItem(itemModel);
        return content;
    }

    public CheckBoxModel checkBox() {
        return checkBox(null, false);
    }

    public CheckBoxModel checkBox(boolean isSelected) {
        return checkBox(null, isSelected);
    }

    public CheckBoxModel checkBox(String caption, boolean isSelected) {
        CheckBoxModel checkBoxModel = create(CheckBoxModel.class);
        checkBoxModel.setCaption(caption);
        checkBoxModel.setSelected(isSelected);
        return checkBoxModel;
    }

    public <T extends ItemModel> T getNodeById(String id) {
        return java.util.Objects.requireNonNull((T) idToItemMap.get(id), "Missing (injected?) node with id: " + id);
    }

    public void receivedButtonClick(IdentifiableModel button) {
        if (modelToEventHandlerMap.containsKey(button)) {
            modelToEventHandlerMap.get(button).run();
        }
    }

    @Deprecated
    public <T extends RemotingEvent> T createEvent(Class<T> eventClass) {
        return beanManager.create(eventClass);
    }

    @Deprecated
    public <T extends RemotingEvent> void sendEvent(T event) {
        model.setEvent(event);
    }

    public SplitPaneModel splitPane(Orientation orientation, SplitPaneItemModel... items) {
        SplitPaneModel model = create(SplitPaneModel.class);
        model.setOrientation(orientation);
        model.getItems().addAll(items);
        return model;
    }

    public PropertySheetModel propertySheet() {
        return create(PropertySheetModel.class);
    }

    public <T extends PropertySheetItemModel> T propertySheetItem(Class<T> itemClass) {
        return create(itemClass);
    }

    public PropertySheetItemGroupModel propertySheetItemGroup(String title) {
        PropertySheetItemGroupModel model = create(PropertySheetItemGroupModel.class);
        model.setExpanded(true);
        model.setName(title);
        return model;
    }

    public ToolBarModel toolBar() {
        return create(ToolBarModel.class);
    }


    public ToolBarModel toolBar(ItemModel... models) {
        ToolBarModel toolBar = toolBar();
        toolBar.getItems().addAll(models);
        return toolBar;
    }

    public ToggleButtonModel toggleButton(String caption) {
        ToggleButtonModel model = toggleButton();
        model.setCaption(caption);
        return model;
    }

    public ToggleButtonModel toggleButton(String caption, String action) {
        ToggleButtonModel model = toggleButton(caption);
        model.setAction(action);
        return model;
    }

    public ToggleButtonModel toggleButton() {
        return create(ToggleButtonModel.class);
    }

    public DocumentViewModel documentView() {
        return create(DocumentViewModel.class);
    }

    public SplitPaneItemModel splitPaneItem(ItemModel content, double dividerPosition) {
        SplitPaneItemModel model = create(SplitPaneItemModel.class);
        model.setContent(content);
        model.setDividerPosition(dividerPosition);
        return model;
    }

    public TextAreaModel textArea() {
        return create(TextAreaModel.class);
    }

    public ProgressIndicatorModel progressIndicator() {
        return create(ProgressIndicatorModel.class);
    }

    public ImageViewModel imageView(String resourcePath) {
        ImageViewModel model = create(ImageViewModel.class);
        model.setResourcePath(resourcePath);
        return model;
    }

    public HyperlinkModel hyperlink(String caption) {
        return hyperlink(caption, (Runnable) null);
    }

    public HyperlinkModel hyperlink(String caption, Runnable handler) {
        HyperlinkModel hyperlinkModel = create(HyperlinkModel.class);
        configureButton(hyperlinkModel, caption, handler);
        return hyperlinkModel;
    }

    public HyperlinkModel hyperlink(String caption, String action) {
        HyperlinkModel hyperlinkModel = hyperlink(caption, (Runnable) null);
        hyperlinkModel.setAction(action);
        return hyperlinkModel;
    }

    public MessagePlaceholder messagePlaceholder(ItemModel forComponent) {
        MessagePlaceholder messagePlaceholder = messagePlaceholder();
        forComponent.setMessageDisplay(messagePlaceholder);
        return messagePlaceholder;
    }

    private MessagePlaceholder messagePlaceholder() {
        return create(MessagePlaceholder.class);
    }

    public DateTimeFieldModel dateTimeField() {
        return create(DateTimeFieldModel.class);
    }

    public AutoCompleteModel airportField() {
        AutoCompleteModel model = autoCompleteField();
        model.setPromptText("ICAO, Name oder Stadt");
        model.setId("airport" + model.getId());
        return model;
    }

    public AutoCompleteModel autoCompleteField() {
        return create(AutoCompleteModel.class);
    }

    public AutoCompleteModel handlerField(AutoCompleteModel atAirport, Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier) {
        return handlerField(atAirport, selectionSupplier, null);
    }

    public AutoCompleteModel handlerField(AutoCompleteModel atAirport, Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, Property<String> captionProperty) {
        AutoCompleteModel model = autoCompleteField();
        model.setPromptText("Firmenname");
        model.setId("handler" + model.getId());
        configureDependsOn(model, atAirport, selectionSupplier, captionProperty);
        return model;
    }

    private void configureDependsOn(AutoCompleteModel modelThat, AutoCompleteModel dependsOn, Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, final Property<String> captionProperty) {
        if (dependsOn == null) {
            return;
        }
        final Property<String> finalCaptionProperty = Optional.ofNullable(captionProperty).orElse(modelThat.promptTextProperty());
        String originalCaptionText = Optional.ofNullable(finalCaptionProperty.get()).orElse("");
        CommonUiHelper.subscribe(dependsOn.selectedProperty(), evt -> {
            AutoCompleteItemModel newValue = evt.getNewValue();
            String newId = Optional.ofNullable(newValue).map(AutoCompleteItemModel::getReference).orElse(null);
            CommonUiHelper.setProperty(modelThat, "masterId", newId);
            modelThat.setSelected(selectionSupplier.apply(newValue));
            modelThat.setDisable(newId == null);
            String captionText = originalCaptionText;
            if (newId != null && newValue.getReference() != null) {
                captionText += " in " + newValue.getCaption();
            }
            finalCaptionProperty.set(captionText);
        });
    }

    public AutoCompleteModel hotelField(AutoCompleteModel atAirport, Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier) {
        return hotelField(atAirport, selectionSupplier, null);
    }

    public AutoCompleteModel hotelField(AutoCompleteModel atAirport, Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, Property<String> captionProperty) {
        AutoCompleteModel model = autoCompleteField();
        model.setPromptText("Hotelname");
        model.setId("hotel" + model.getId());
        configureDependsOn(model, atAirport, selectionSupplier, captionProperty);
        return model;
    }

    public AutoCompleteItemModel autoCompleteItem(String reference, String caption) {
        AutoCompleteItemModel model = create(AutoCompleteItemModel.class);
        model.setReference(reference);
        model.setCaption(caption);
        return model;
    }

    public MapViewModel mapView() {
        return create(MapViewModel.class);
    }

    public ChoiceBoxModel choiceBox() {
        return create(ChoiceBoxModel.class);
    }

    public ScrollPaneModel scrollPane(ItemModel content) {
        ScrollPaneModel model = create(ScrollPaneModel.class);
        model.setContent(content);
        return model;
    }

    public HBoxItemModel hBoxItem(ItemModel content) {
        return hBoxItem(content, Priority.NEVER);
    }

    public HBoxItemModel hBoxItem(ItemModel content, Priority hGrow) {
        HBoxItemModel model = create(HBoxItemModel.class);
        model.setItem(content);
        model.sethGrow(hGrow);
        return model;
    }

    public VBoxItemModel vBoxItem(ItemModel content) {
        return vBoxItem(content, Priority.NEVER);
    }

    public VBoxItemModel vBoxItem(ItemModel content, Priority vGrow) {
        VBoxItemModel model = create(VBoxItemModel.class);
        model.setItem(content);
        model.setvGrow(vGrow);
        return model;
    }

    public PaxCodeFieldModel paxCodeField() {
        return create(PaxCodeFieldModel.class);
    }

    public TableModel table() {
        return create(TableModel.class);
    }

    public TableStringColumnModel tableStringColum(String caption, Double prefWidth) {
        return tableStringColum(null, caption, prefWidth);
    }

    public TableStringColumnModel tableStringColum(String reference, String caption, Double prefWidth) {
        TableStringColumnModel column = create(TableStringColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    private void configureTableColumn(String reference, TableColumnModel column, String caption, Double prefWidth) {
        column.setReference(reference);
        column.setCaption(caption);
        column.setPrefWidth(prefWidth);
    }

    public TableChoiceBoxColumnModel tableChoiceBoxColum(String reference, String caption, Double prefWidth) {
        TableChoiceBoxColumnModel column = create(TableChoiceBoxColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    public TableCheckBoxColumnModel tableCheckBoxColum(String reference, String caption, Double prefWidth) {
        TableCheckBoxColumnModel column = create(TableCheckBoxColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    public TableInstantColumnModel tableInstantColum(String caption, Double prefWidth) {
        TableInstantColumnModel column = create(TableInstantColumnModel.class);
        configureTableColumn(null, column, caption, prefWidth);
        return column;
    }


    public TableColumnModel tableNumberColum(String caption, Double prefWidth) {
        TableIntegerColumnModel column = create(TableIntegerColumnModel.class);
        configureTableColumn(null, column, caption, prefWidth);
        return column;
    }

    public BreadCrumbBarModel breadCrumbBar() {
        return create(BreadCrumbBarModel.class);
    }

    public BreadCrumbItemModel breadCrumbBarItem(String caption, String reference) {
        BreadCrumbItemModel item = breadCrumbBarItem(caption);
        item.setReference(reference);
        return item;
    }

    public BreadCrumbItemModel breadCrumbBarItem(String caption) {
        BreadCrumbItemModel model = create(BreadCrumbItemModel.class);
        model.setCaption(caption);
        return model;
    }

    public TableRowModel tableRow() {
        return create(TableRowModel.class);
    }

    public TableStringCellModel tableStringCell(String value) {
        return tableStringCell(null, value);
    }

    public TableStringCellModel tableStringCell(String reference, String value) {
        TableStringCellModel cellModel = create(TableStringCellModel.class);
        cellModel.setReference(reference);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableStringCellModel tableStringCell() {
        return create(TableStringCellModel.class);
    }

    public TableInstantCellModel tableInstantCell(Instant value) {
        TableInstantCellModel cellModel = create(TableInstantCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableInstantCellModel tableInstantCell() {
        return create(TableInstantCellModel.class);
    }

    public TableChoiceBoxCellModel tableChoiceBoxCell(ChoiceBoxItemModel value) {
        return tableChoiceBoxCell(null, value);
    }

    public TableChoiceBoxCellModel tableChoiceBoxCell(String reference, ChoiceBoxItemModel value) {
        TableChoiceBoxCellModel cellModel = create(TableChoiceBoxCellModel.class);
        cellModel.setReference(reference);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableCheckBoxCellModel tableCheckBoxCell(Boolean value) {
        TableCheckBoxCellModel cellModel = create(TableCheckBoxCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableIntegerCellModel tableIntegerCell(Integer value) {
        TableIntegerCellModel cellModel = create(TableIntegerCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public AutoCompleteModel companyField() {
        AutoCompleteModel field = autoCompleteField();
        field.setId("company" + field.getId());
        field.setPromptText("Firma oder Kurzname der Firma");
        return field;
    }

    public AutoCompleteModel aircraftField() {
        return aircraftField(null);
    }

    public AutoCompleteModel aircraftField(AutoCompleteModel dependsOn) {
        AutoCompleteModel field = autoCompleteField();
        field.setId("aircraft" + field.getId());
        field.setPromptText("Luftfahrzeug bzw. Kennzeichen");
        configureDependsOn(field, dependsOn);
        return field;
    }

    private void configureDependsOn(AutoCompleteModel modelThat, AutoCompleteModel dependsOn) {
        configureDependsOn(modelThat, dependsOn, (dependsOnNewValue) -> null, new MockedProperty<>());
    }

    public AutoCompleteModel personField(String type) {
        return personField(type, null);
    }

    public AutoCompleteModel personField(String type, AutoCompleteModel dependsOn) {
        Objects.requireNonNull(type);
        AutoCompleteModel field = autoCompleteField();
        field.setId(type + field.getId());
        field.setPromptText("Vorname, Nachname oder Initialien");
        configureDependsOn(field, dependsOn);
        return field;
    }



    public ItemModel separator(Orientation orientation) {
        SeparatorModel separatorModel = create(SeparatorModel.class);
        separatorModel.setOrientation(orientation);
        return separatorModel;
    }

    public FlowPaneItemModel flowPaneItem(ItemModel content) {
        FlowPaneItemModel model = create(FlowPaneItemModel.class);
        model.setItem(content);
        return model;
    }

    public FlowPaneModel flowPane(Orientation orientation) {
        FlowPaneModel pane = create(FlowPaneModel.class);
        pane.setOrientation(orientation);
        return pane;
    }

    public BorderPaneModel borderPane(ItemModel content) {
        BorderPaneModel borderPane = borderPane();
        borderPane.setCenter(content);
        return borderPane;
    }

    public BorderPaneModel borderPane() {
        return create(BorderPaneModel.class);
    }

    public ButtonModel button(String caption) {
        return button(caption, (Runnable) null);
    }

    public UploadButtonModel uploadButton(String caption, String onUploadBeginAction, String onUploadFinishedAction, String onUploadFailedAction) {
        return uploadButton(caption, null, onUploadBeginAction, onUploadFinishedAction, onUploadFailedAction);
    }

    public UploadButtonModel uploadButton(String caption, String uploadUrl, String onUploadBeginAction, String onUploadFinishedAction, String onUploadFailedAction) {
        UploadButtonModel buttonModel = create(UploadButtonModel.class);
        buttonModel.setUploadUrl(uploadUrl);
        buttonModel.setOnUploadBeginAction(onUploadBeginAction);
        buttonModel.setOnUploadFinishedAction(onUploadFinishedAction);
        buttonModel.setOnUploadFailedAction(onUploadFailedAction);
        configureButton(buttonModel, caption, null);
        return buttonModel;
    }

    // TODO: Sollte irgendwie anders gehen?
    public void removeId(String key) {
        idToItemMap.remove(key);
    }

    public CardPaneModel cardPane() {
        return create(CardPaneModel.class);
    }

    public CardPaneItemModel cardPaneItem(ItemModel content) {
        Objects.requireNonNull(content);
        CardPaneItemModel model = create(CardPaneItemModel.class);
        model.setItem(content);
        return model;
    }

    public HiddenSidesPaneModel hiddenSidesPane() {
        return create(HiddenSidesPaneModel.class);
    }

    public ChoiceBoxItemModel choiceBoxItem(String reference, String caption) {
        ChoiceBoxItemModel model = create(ChoiceBoxItemModel.class);
        model.setReference(reference);
        model.setCaption(caption);
        return model;
    }

    public ListViewModel listView() {
        return create(ListViewModel.class);
    }

    public ListViewItemModel listViewItem() {
        return create(ListViewItemModel.class);
    }

    public NestedMenuButtonModel nestedMenuButton(String caption) {
        NestedMenuButtonModel buttonModel = create(NestedMenuButtonModel.class);
        buttonModel.setCaption(caption);
        return buttonModel;
    }

    public MenuItemModel menuItem(String caption) {
        return menuItem(caption, null);
    }

    public MenuItemModel menuItem(String caption, String action) {
        MenuItemModel model = create(MenuItemModel.class);
        model.setCaption(caption);
        model.setAction(action);
        return model;
    }

    public UnexpectedErrorDialogModel unexpectedErrorDialog(ItemModel owner, String exceptionText) {
        UnexpectedErrorDialogModel dialog = create(UnexpectedErrorDialogModel.class);
        dialog.setOwner(owner);
        dialog.setExceptionText(exceptionText);
        return dialog;
    }

    public QualifiedErrorDialogModel qualifiedErrorDialog(ItemModel owner, String headerText, String contentText, String exceptionText) {
        QualifiedErrorDialogModel dialog = create(QualifiedErrorDialogModel.class);
        dialog.setOwner(owner);
        dialog.setContentText(contentText);
        dialog.setHeaderText(headerText);
        dialog.setExceptionText(exceptionText);
        return dialog;
    }

    public ConfirmationDialogModel confirmationDialog(ItemModel owner) {
        ConfirmationDialogModel dialog = create(ConfirmationDialogModel.class);
        dialog.setOwner(owner);
        return dialog;
    }

    public SaveFileDialogModel saveFileDialog(ItemModel owner, String title, DocumentData saveThis) {
        SaveFileDialogModel dialog = create(SaveFileDialogModel.class);
        dialog.setOwner(owner);
        dialog.setTitle(title);
        dialog.setSaveThis(saveThis);
        return dialog;
    }

    public ItemModel customComponent(String type) {
        CustomComponentModel model = create(CustomComponentModel.class);
        model.setType(type);
        return model;
    }

    public NotificationPaneModel notificationPane() {
        return create(NotificationPaneModel.class);
    }

    public ShutdownDialogModel shutdownDialog() {
        return create(ShutdownDialogModel.class);
    }

    public ListSelectionViewModel listSelectionView() {
        return create(ListSelectionViewModel.class);
    }

    public ListSelectionViewItemModel listSelectionViewItem() {
        return create(ListSelectionViewItemModel.class);
    }

    public SplitMenuButtonModel splitMenuButton() {
        return create(SplitMenuButtonModel.class);
    }

    public RadioButtonModel radioButton(String caption) {
        return radioButton(caption, null);
    }

    public RadioButtonModel radioButton(String caption, String action) {
        RadioButtonModel model = create(RadioButtonModel.class);
        model.setAction(action);
        model.setCaption(caption);
        return model;
    }

    public InfoDialogModel infoDialog(ItemModel owner) {
        InfoDialogModel dialog = create(InfoDialogModel.class);
        dialog.setOwner(owner);
        return dialog;
    }

    public SegmentedButtonModel segmentedButton() {
        return create(SegmentedButtonModel.class);
    }

    public ButtonModel button() {
        return create(ButtonModel.class);
    }

    public CheckListViewModel checkListView() {
        return create(CheckListViewModel.class);
    }

    public ListSelectionViewItemModel listSelectionViewItem(String reference, String caption) {
        ListSelectionViewItemModel itemModel = create(ListSelectionViewItemModel.class);
        itemModel.setReference(reference);
        itemModel.setCaption(caption);
        return itemModel;
    }

    public CustomDialogModel customDialog(String title) {
        CustomDialogModel model = create(CustomDialogModel.class);
        model.setTitle(title);
        return model;
    }

    public MigPaneModel migPane() {
        return migPane(null, null, null);
    }

    public MigPaneModel migPane(String layoutConstraints, String columnConstraints, String rowConstraints) {
        MigPaneModel migPaneModel = create(MigPaneModel.class);
        migPaneModel.setLayoutConstraints(layoutConstraints);
        migPaneModel.setColumnConstraints(columnConstraints);
        migPaneModel.setRowConstraints(rowConstraints);
        return migPaneModel;
    }

    public MigPaneItemModel migPaneItem(ItemModel content, String constraints) {
        MigPaneItemModel model = create(MigPaneItemModel.class);
        model.setItem(content);
        model.setConstraints(constraints);
        return model;
    }

    public MigPaneItemModel migPaneItem(ItemModel content) {
        return migPaneItem(content, null);
    }

    public HBoxModel hBox(ItemModel... items) {
        HBoxModel hBox = hBox();
        Arrays.stream(items).map(this::hBoxItem).forEach(hBox::add);
        return hBox;
    }

    public VBoxModel vBox(ItemModel... items) {
        VBoxModel vBox = vBox();
        Arrays.stream(items).map(this::vBoxItem).forEach(vBox::add);
        return vBox;
    }

    public PasswordFieldModel passwordField() {
        return create(PasswordFieldModel.class);
    }

    public TabPaneModel tabPane() {
        return create(TabPaneModel.class);
    }

    public TabPaneItemModel tabPaneItem(ItemModel content, String caption) {
        TabPaneItemModel item = create(TabPaneItemModel.class);
        item.setContent(content);
        item.setCaption(caption);
        return item;
    }
}
