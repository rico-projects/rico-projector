package dev.rico.internal.server.projector;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.BorderPaneModel;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.CheckBoxModel;
import dev.rico.internal.projector.ui.DateTimeFieldModel;
import dev.rico.internal.projector.ui.HyperlinkModel;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ImageViewModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.LabelModel;
import dev.rico.internal.projector.ui.PasswordFieldModel;
import dev.rico.internal.projector.ui.RadioButtonModel;
import dev.rico.internal.projector.ui.ScrollPaneModel;
import dev.rico.internal.projector.ui.SeparatorModel;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.internal.projector.ui.TitledPaneModel;
import dev.rico.internal.projector.ui.ToggleButtonModel;
import dev.rico.internal.projector.ui.ToolBarModel;
import dev.rico.internal.projector.ui.box.HBoxItemModel;
import dev.rico.internal.projector.ui.box.HBoxModel;
import dev.rico.internal.projector.ui.box.VBoxItemModel;
import dev.rico.internal.projector.ui.box.VBoxModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.internal.projector.ui.dialog.ConfirmationDialogModel;
import dev.rico.internal.projector.ui.dialog.CustomDialogModel;
import dev.rico.internal.projector.ui.dialog.InfoDialogModel;
import dev.rico.internal.projector.ui.dialog.QualifiedErrorDialogModel;
import dev.rico.internal.projector.ui.dialog.ShutdownDialogModel;
import dev.rico.internal.projector.ui.dialog.UnexpectedErrorDialogModel;
import dev.rico.internal.projector.ui.flowpane.FlowPaneItemModel;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import dev.rico.internal.projector.ui.listview.ListViewItemModel;
import dev.rico.internal.projector.ui.listview.ListViewModel;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneModel;
import dev.rico.internal.projector.ui.table.TableCheckBoxCellModel;
import dev.rico.internal.projector.ui.table.TableCheckBoxColumnModel;
import dev.rico.internal.projector.ui.table.TableChoiceBoxCellModel;
import dev.rico.internal.projector.ui.table.TableChoiceBoxColumnModel;
import dev.rico.internal.projector.ui.table.TableColumnModel;
import dev.rico.internal.projector.ui.table.TableInstantCellModel;
import dev.rico.internal.projector.ui.table.TableInstantColumnModel;
import dev.rico.internal.projector.ui.table.TableIntegerCellModel;
import dev.rico.internal.projector.ui.table.TableIntegerColumnModel;
import dev.rico.internal.projector.ui.table.TableModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import dev.rico.internal.projector.ui.table.TableStringCellModel;
import dev.rico.internal.projector.ui.table.TableStringColumnModel;
import dev.rico.internal.projector.ui.tabpane.TabPaneItemModel;
import dev.rico.internal.projector.ui.tabpane.TabPaneModel;
import dev.rico.internal.remoting.MockedProperty;
import dev.rico.remoting.BeanManager;
import dev.rico.remoting.Property;
import javafx.geometry.Orientation;
import javafx.scene.layout.Priority;
import to.remove.DocumentData;
import to.remove.SaveFileDialogModel;
import to.remove.ui.DocumentViewModel;
import to.remove.ui.FuelFieldModel;
import to.remove.ui.HiddenSidesPaneModel;
import to.remove.ui.MapViewModel;
import to.remove.ui.MessagePlaceholder;
import to.remove.ui.NotificationPaneModel;
import to.remove.ui.PaxCodeFieldModel;
import to.remove.ui.ProgressIndicatorModel;
import to.remove.ui.SegmentedButtonModel;
import to.remove.ui.SplitMenuButtonModel;
import to.remove.ui.UploadButtonModel;
import to.remove.ui.autocompletion.AutoCompleteItemModel;
import to.remove.ui.autocompletion.AutoCompleteModel;
import to.remove.ui.breadcrumbbar.BreadCrumbBarModel;
import to.remove.ui.breadcrumbbar.BreadCrumbItemModel;
import to.remove.ui.cardpane.CardPaneItemModel;
import to.remove.ui.cardpane.CardPaneModel;
import to.remove.ui.checklistview.CheckListViewModel;
import to.remove.ui.listselectionview.ListSelectionViewItemModel;
import to.remove.ui.listselectionview.ListSelectionViewModel;
import to.remove.ui.menubutton.MenuButtonItemModel;
import to.remove.ui.menubutton.MenuButtonModel;
import to.remove.ui.migpane.MigPaneItemModel;
import to.remove.ui.migpane.MigPaneModel;
import to.remove.ui.nestedmenubutton.NestedMenuButtonModel;
import to.remove.ui.propertysheet.PropertySheetItemGroupModel;
import to.remove.ui.propertysheet.PropertySheetItemModel;
import to.remove.ui.propertysheet.PropertySheetModel;

@SuppressWarnings("WeakerAccess")
public class ServerUiManager extends BaseServerUiManager {


    public ServerUiManager(final BeanManager beanManager) {
        super(beanManager);
    }

    public VBoxModel vBox() {
        return create(VBoxModel.class);
    }

    public VBoxModel vBox(final VBoxItemModel... childs) {
        final VBoxModel vBox = vBox();
        vBox.addAll(childs);
        return vBox;
    }

    public HBoxModel hBox() {
        return create(HBoxModel.class);
    }

    public LabelModel label() {
        return label(null);
    }

    public LabelModel label(final String text) {
        final LabelModel labelModel = create(LabelModel.class);
        labelModel.setWrapText(true);
        labelModel.setText(text);
        return labelModel;
    }

    public FuelFieldModel fuelField() {
        final FuelFieldModel fuelFieldModel = create(FuelFieldModel.class);
        fuelFieldModel.setPromptText("Min, Max, Remaining");
        return fuelFieldModel;
    }

    public TextFieldModel textField() {
        return create(TextFieldModel.class);
    }

    public MenuButtonModel menuButton() {
        return menuButton(null);
    }

    public MenuButtonModel menuButton(final String caption) {
        final MenuButtonModel button = create(MenuButtonModel.class);
        button.setCaption(caption);
        return button;
    }

    public MenuButtonItemModel menuButtonItem(final String caption) {
        return menuButtonItem(caption, (Runnable) null);
    }

    public MenuButtonItemModel menuButtonItem(final String caption, final String action) {
        final MenuButtonItemModel model = menuButtonItem(caption, (Runnable) null);
        model.setAction(action);
        return model;
    }

    public MenuButtonItemModel menuButtonItem(final String caption, final Runnable handler) {
        final MenuButtonItemModel model = create(MenuButtonItemModel.class);
        model.setCaption(caption);
        maybeInstallActionHandler(model, handler);
        return model;
    }

    // TODO: Auf neues Action-System umstellen
    @Deprecated
    public void maybeInstallActionHandler(final IdentifiableModel model, final Runnable handler) {
        if (handler != null && model != null) {
            installActionHandler(model, handler);
        }
    }

    public ButtonModel button(final Runnable handler) {
        return button(null, handler);
    }

    public ButtonModel button(final String caption, final Runnable handler) {
        final ButtonModel buttonModel = create(ButtonModel.class);
        return configureButton(buttonModel, caption, handler);
    }

    public ButtonModel button(final String caption, final String action) {
        final ButtonModel button = button(caption, (Runnable) null);
        button.setAction(action);
        return button;
    }

    protected ButtonModel configureButton(final ButtonModel buttonModel, final String caption, final Runnable handler) {
        buttonModel.setCaption(caption);
        maybeInstallActionHandler(buttonModel, handler);
        return buttonModel;
    }

    public TitledPaneModel titledPane(final String title) {
        final TitledPaneModel titledPaneModel = create(TitledPaneModel.class);
        titledPaneModel.setTitle(title);
        return titledPaneModel;
    }

    public GridPaneModel gridPane() {
        return create(GridPaneModel.class);
    }

    public GridPaneItemModel gridPaneContent(final int col, final int row, final ItemModel itemModel) {
        final GridPaneItemModel content = create(GridPaneItemModel.class);
        content.setCol(col);
        content.setRow(row);
        content.setItem(itemModel);
        return content;
    }

    public GridPaneItemModel gridPaneContent(final int col, final int row, final int colSpan, final int rowSpan, final ItemModel itemModel) {
        final GridPaneItemModel content = create(GridPaneItemModel.class);
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

    public CheckBoxModel checkBox(final boolean isSelected) {
        return checkBox(null, isSelected);
    }

    public CheckBoxModel checkBox(final String caption, final boolean isSelected) {
        final CheckBoxModel checkBoxModel = create(CheckBoxModel.class);
        checkBoxModel.setCaption(caption);
        checkBoxModel.setSelected(isSelected);
        return checkBoxModel;
    }

    public SplitPaneModel splitPane(final Orientation orientation, final SplitPaneItemModel... items) {
        final SplitPaneModel model = create(SplitPaneModel.class);
        model.setOrientation(orientation);
        model.getItems().addAll(items);
        return model;
    }

    public PropertySheetModel propertySheet() {
        return create(PropertySheetModel.class);
    }

    public <T extends PropertySheetItemModel> T propertySheetItem(final Class<T> itemClass) {
        return create(itemClass);
    }

    public PropertySheetItemGroupModel propertySheetItemGroup(final String title) {
        final PropertySheetItemGroupModel model = create(PropertySheetItemGroupModel.class);
        model.setExpanded(true);
        model.setName(title);
        return model;
    }

    public ToolBarModel toolBar() {
        return create(ToolBarModel.class);
    }

    public ToolBarModel toolBar(final ItemModel... models) {
        final ToolBarModel toolBar = toolBar();
        toolBar.getItems().addAll(models);
        return toolBar;
    }

    public ToggleButtonModel toggleButton(final String caption) {
        final ToggleButtonModel model = toggleButton();
        model.setCaption(caption);
        return model;
    }

    public ToggleButtonModel toggleButton(final String caption, final String action) {
        final ToggleButtonModel model = toggleButton(caption);
        model.setAction(action);
        return model;
    }

    public ToggleButtonModel toggleButton() {
        return create(ToggleButtonModel.class);
    }

    public DocumentViewModel documentView() {
        return create(DocumentViewModel.class);
    }

    public SplitPaneItemModel splitPaneItem(final ItemModel content, final double dividerPosition) {
        final SplitPaneItemModel model = create(SplitPaneItemModel.class);
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

    public ImageViewModel imageView(final String resourcePath) {
        final ImageViewModel model = create(ImageViewModel.class);
        model.setResourcePath(resourcePath);
        return model;
    }

    public HyperlinkModel hyperlink(final String caption) {
        return hyperlink(caption, (Runnable) null);
    }

    public HyperlinkModel hyperlink(final String caption, final Runnable handler) {
        final HyperlinkModel hyperlinkModel = create(HyperlinkModel.class);
        configureButton(hyperlinkModel, caption, handler);
        return hyperlinkModel;
    }

    public HyperlinkModel hyperlink(final String caption, final String action) {
        final HyperlinkModel hyperlinkModel = hyperlink(caption, (Runnable) null);
        hyperlinkModel.setAction(action);
        return hyperlinkModel;
    }

    private MessagePlaceholder messagePlaceholder() {
        return create(MessagePlaceholder.class);
    }

    public DateTimeFieldModel dateTimeField() {
        return create(DateTimeFieldModel.class);
    }

    public AutoCompleteModel airportField() {
        final AutoCompleteModel model = autoCompleteField();
        model.setPromptText("ICAO, Name oder Stadt");
        model.setId("airport" + model.getId());
        return model;
    }

    public AutoCompleteModel autoCompleteField() {
        return create(AutoCompleteModel.class);
    }

    public AutoCompleteModel handlerField(final AutoCompleteModel atAirport, final Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier) {
        return handlerField(atAirport, selectionSupplier, null);
    }

    public AutoCompleteModel handlerField(final AutoCompleteModel atAirport, final Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, final Property<String> captionProperty) {
        final AutoCompleteModel model = autoCompleteField();
        model.setPromptText("Firmenname");
        model.setId("handler" + model.getId());
        configureDependsOn(model, atAirport, selectionSupplier, captionProperty);
        return model;
    }

    private void configureDependsOn(final AutoCompleteModel modelThat, final AutoCompleteModel dependsOn, final Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, final Property<String> captionProperty) {
        if (dependsOn == null) {
            return;
        }
        final Property<String> finalCaptionProperty = Optional.ofNullable(captionProperty).orElse(modelThat.promptTextProperty());
        final String originalCaptionText = Optional.ofNullable(finalCaptionProperty.get()).orElse("");
        CommonUiHelper.subscribe(dependsOn.selectedProperty(), evt -> {
            final AutoCompleteItemModel newValue = evt.getNewValue();
            final String newId = Optional.ofNullable(newValue).map(AutoCompleteItemModel::getReference).orElse(null);
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

    public AutoCompleteModel hotelField(final AutoCompleteModel atAirport, final Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier) {
        return hotelField(atAirport, selectionSupplier, null);
    }

    public AutoCompleteModel hotelField(final AutoCompleteModel atAirport, final Function<AutoCompleteItemModel, AutoCompleteItemModel> selectionSupplier, final Property<String> captionProperty) {
        final AutoCompleteModel model = autoCompleteField();
        model.setPromptText("Hotelname");
        model.setId("hotel" + model.getId());
        configureDependsOn(model, atAirport, selectionSupplier, captionProperty);
        return model;
    }

    public AutoCompleteItemModel autoCompleteItem(final String reference, final String caption) {
        final AutoCompleteItemModel model = create(AutoCompleteItemModel.class);
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

    public ScrollPaneModel scrollPane(final ItemModel content) {
        final ScrollPaneModel model = create(ScrollPaneModel.class);
        model.setContent(content);
        return model;
    }

    public HBoxItemModel hBoxItem(final ItemModel content) {
        return hBoxItem(content, Priority.NEVER);
    }

    public HBoxItemModel hBoxItem(final ItemModel content, final Priority hGrow) {
        final HBoxItemModel model = create(HBoxItemModel.class);
        model.setItem(content);
        model.sethGrow(hGrow);
        return model;
    }

    public VBoxItemModel vBoxItem(final ItemModel content) {
        return vBoxItem(content, Priority.NEVER);
    }

    public VBoxItemModel vBoxItem(final ItemModel content, final Priority vGrow) {
        final VBoxItemModel model = create(VBoxItemModel.class);
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

    public TableStringColumnModel tableStringColum(final String caption, final Double prefWidth) {
        return tableStringColum(null, caption, prefWidth);
    }

    public TableStringColumnModel tableStringColum(final String reference, final String caption, final Double prefWidth) {
        final TableStringColumnModel column = create(TableStringColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    protected void configureTableColumn(final String reference, final TableColumnModel column, final String caption, final Double prefWidth) {
        column.setReference(reference);
        column.setCaption(caption);
        column.setPrefWidth(prefWidth);
    }

    public TableChoiceBoxColumnModel tableChoiceBoxColum(final String reference, final String caption, final Double prefWidth) {
        final TableChoiceBoxColumnModel column = create(TableChoiceBoxColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    public TableCheckBoxColumnModel tableCheckBoxColum(final String reference, final String caption, final Double prefWidth) {
        final TableCheckBoxColumnModel column = create(TableCheckBoxColumnModel.class);
        configureTableColumn(reference, column, caption, prefWidth);
        return column;
    }

    public TableInstantColumnModel tableInstantColum(final String caption, final Double prefWidth) {
        final TableInstantColumnModel column = create(TableInstantColumnModel.class);
        configureTableColumn(null, column, caption, prefWidth);
        return column;
    }

    public TableColumnModel tableNumberColum(final String caption, final Double prefWidth) {
        final TableIntegerColumnModel column = create(TableIntegerColumnModel.class);
        configureTableColumn(null, column, caption, prefWidth);
        return column;
    }

    public BreadCrumbBarModel breadCrumbBar() {
        return create(BreadCrumbBarModel.class);
    }

    public BreadCrumbItemModel breadCrumbBarItem(final String caption, final String reference) {
        final BreadCrumbItemModel item = breadCrumbBarItem(caption);
        item.setReference(reference);
        return item;
    }

    public BreadCrumbItemModel breadCrumbBarItem(final String caption) {
        final BreadCrumbItemModel model = create(BreadCrumbItemModel.class);
        model.setCaption(caption);
        return model;
    }

    public TableRowModel tableRow() {
        return create(TableRowModel.class);
    }

    public TableStringCellModel tableStringCell(final String value) {
        return tableStringCell(null, value);
    }

    public TableStringCellModel tableStringCell(final String reference, final String value) {
        final TableStringCellModel cellModel = create(TableStringCellModel.class);
        cellModel.setReference(reference);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableStringCellModel tableStringCell() {
        return create(TableStringCellModel.class);
    }

    public TableInstantCellModel tableInstantCell(final Instant value) {
        final TableInstantCellModel cellModel = create(TableInstantCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableInstantCellModel tableInstantCell() {
        return create(TableInstantCellModel.class);
    }

    public TableChoiceBoxCellModel tableChoiceBoxCell(final ChoiceBoxItemModel value) {
        return tableChoiceBoxCell(null, value);
    }

    public TableChoiceBoxCellModel tableChoiceBoxCell(final String reference, final ChoiceBoxItemModel value) {
        final TableChoiceBoxCellModel cellModel = create(TableChoiceBoxCellModel.class);
        cellModel.setReference(reference);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableCheckBoxCellModel tableCheckBoxCell(final Boolean value) {
        final TableCheckBoxCellModel cellModel = create(TableCheckBoxCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public TableIntegerCellModel tableIntegerCell(final Integer value) {
        final TableIntegerCellModel cellModel = create(TableIntegerCellModel.class);
        cellModel.setValue(value);
        return cellModel;
    }

    public AutoCompleteModel companyField() {
        final AutoCompleteModel field = autoCompleteField();
        field.setId("company" + field.getId());
        field.setPromptText("Firma oder Kurzname der Firma");
        return field;
    }

    public AutoCompleteModel aircraftField() {
        return aircraftField(null);
    }

    public AutoCompleteModel aircraftField(final AutoCompleteModel dependsOn) {
        final AutoCompleteModel field = autoCompleteField();
        field.setId("aircraft" + field.getId());
        field.setPromptText("Luftfahrzeug bzw. Kennzeichen");
        configureDependsOn(field, dependsOn);
        return field;
    }

    private void configureDependsOn(final AutoCompleteModel modelThat, final AutoCompleteModel dependsOn) {
        configureDependsOn(modelThat, dependsOn, (dependsOnNewValue) -> null, new MockedProperty<>());
    }

    public AutoCompleteModel personField(final String type) {
        return personField(type, null);
    }

    public AutoCompleteModel personField(final String type, final AutoCompleteModel dependsOn) {
        Objects.requireNonNull(type);
        final AutoCompleteModel field = autoCompleteField();
        field.setId(type + field.getId());
        field.setPromptText("Vorname, Nachname oder Initialien");
        configureDependsOn(field, dependsOn);
        return field;
    }

    public ItemModel separator(final Orientation orientation) {
        final SeparatorModel separatorModel = create(SeparatorModel.class);
        separatorModel.setOrientation(orientation);
        return separatorModel;
    }

    public FlowPaneItemModel flowPaneItem(final ItemModel content) {
        final FlowPaneItemModel model = create(FlowPaneItemModel.class);
        model.setItem(content);
        return model;
    }

    public FlowPaneModel flowPane(final Orientation orientation) {
        final FlowPaneModel pane = create(FlowPaneModel.class);
        pane.setOrientation(orientation);
        return pane;
    }

    public BorderPaneModel borderPane(final ItemModel content) {
        final BorderPaneModel borderPane = borderPane();
        borderPane.setCenter(content);
        return borderPane;
    }

    public BorderPaneModel borderPane() {
        return create(BorderPaneModel.class);
    }

    public ButtonModel button(final String caption) {
        return button(caption, (Runnable) null);
    }

    public UploadButtonModel uploadButton(final String caption, final String onUploadBeginAction, final String onUploadFinishedAction, final String onUploadFailedAction) {
        return uploadButton(caption, null, onUploadBeginAction, onUploadFinishedAction, onUploadFailedAction);
    }

    public UploadButtonModel uploadButton(final String caption, final String uploadUrl, final String onUploadBeginAction, final String onUploadFinishedAction, final String onUploadFailedAction) {
        final UploadButtonModel buttonModel = create(UploadButtonModel.class);
        buttonModel.setUploadUrl(uploadUrl);
        buttonModel.setOnUploadBeginAction(onUploadBeginAction);
        buttonModel.setOnUploadFinishedAction(onUploadFinishedAction);
        buttonModel.setOnUploadFailedAction(onUploadFailedAction);
        configureButton(buttonModel, caption, null);
        return buttonModel;
    }

    public CardPaneModel cardPane() {
        return create(CardPaneModel.class);
    }

    public CardPaneItemModel cardPaneItem(final ItemModel content) {
        Objects.requireNonNull(content);
        final CardPaneItemModel model = create(CardPaneItemModel.class);
        model.setItem(content);
        return model;
    }

    public HiddenSidesPaneModel hiddenSidesPane() {
        return create(HiddenSidesPaneModel.class);
    }

    public ChoiceBoxItemModel choiceBoxItem(final String reference, final String caption) {
        final ChoiceBoxItemModel model = create(ChoiceBoxItemModel.class);
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

    public NestedMenuButtonModel nestedMenuButton(final String caption) {
        final NestedMenuButtonModel buttonModel = create(NestedMenuButtonModel.class);
        buttonModel.setCaption(caption);
        return buttonModel;
    }

    public MenuItemModel menuItem(final String caption) {
        return menuItem(caption, null);
    }

    public MenuItemModel menuItem(final String caption, final String action) {
        final MenuItemModel model = create(MenuItemModel.class);
        model.setCaption(caption);
        model.setAction(action);
        return model;
    }

    public UnexpectedErrorDialogModel unexpectedErrorDialog(final ItemModel owner, final String exceptionText) {
        final UnexpectedErrorDialogModel dialog = create(UnexpectedErrorDialogModel.class);
        dialog.setOwner(owner);
        dialog.setExceptionText(exceptionText);
        return dialog;
    }

    public QualifiedErrorDialogModel qualifiedErrorDialog(final ItemModel owner, final String headerText, final String contentText, final String exceptionText) {
        final QualifiedErrorDialogModel dialog = create(QualifiedErrorDialogModel.class);
        dialog.setOwner(owner);
        dialog.setContentText(contentText);
        dialog.setHeaderText(headerText);
        dialog.setExceptionText(exceptionText);
        return dialog;
    }

    public ConfirmationDialogModel confirmationDialog(final ItemModel owner) {
        final ConfirmationDialogModel dialog = create(ConfirmationDialogModel.class);
        dialog.setOwner(owner);
        return dialog;
    }

    public SaveFileDialogModel saveFileDialog(final ItemModel owner, final String title, final DocumentData saveThis) {
        final SaveFileDialogModel dialog = create(SaveFileDialogModel.class);
        dialog.setOwner(owner);
        dialog.setTitle(title);
        dialog.setSaveThis(saveThis);
        return dialog;
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

    public RadioButtonModel radioButton(final String caption) {
        return radioButton(caption, null);
    }

    public RadioButtonModel radioButton(final String caption, final String action) {
        final RadioButtonModel model = create(RadioButtonModel.class);
        model.setAction(action);
        model.setCaption(caption);
        return model;
    }

    public InfoDialogModel infoDialog(final ItemModel owner) {
        final InfoDialogModel dialog = create(InfoDialogModel.class);
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

    public ListSelectionViewItemModel listSelectionViewItem(final String reference, final String caption) {
        final ListSelectionViewItemModel itemModel = create(ListSelectionViewItemModel.class);
        itemModel.setReference(reference);
        itemModel.setCaption(caption);
        return itemModel;
    }

    public CustomDialogModel customDialog(final String title) {
        final CustomDialogModel model = create(CustomDialogModel.class);
        model.setTitle(title);
        return model;
    }

    public MigPaneModel migPane() {
        return migPane(null, null, null);
    }

    public MigPaneModel migPane(final String layoutConstraints, final String columnConstraints, final String rowConstraints) {
        final MigPaneModel migPaneModel = create(MigPaneModel.class);
        migPaneModel.setLayoutConstraints(layoutConstraints);
        migPaneModel.setColumnConstraints(columnConstraints);
        migPaneModel.setRowConstraints(rowConstraints);
        return migPaneModel;
    }

    public MigPaneItemModel migPaneItem(final ItemModel content, final String constraints) {
        final MigPaneItemModel model = create(MigPaneItemModel.class);
        model.setItem(content);
        model.setConstraints(constraints);
        return model;
    }

    public MigPaneItemModel migPaneItem(final ItemModel content) {
        return migPaneItem(content, null);
    }

    public HBoxModel hBox(final ItemModel... items) {
        final HBoxModel hBox = hBox();
        Arrays.stream(items).map(this::hBoxItem).forEach(hBox::add);
        return hBox;
    }

    public VBoxModel vBox(final ItemModel... items) {
        final VBoxModel vBox = vBox();
        Arrays.stream(items).map(this::vBoxItem).forEach(vBox::add);
        return vBox;
    }

    public PasswordFieldModel passwordField() {
        return create(PasswordFieldModel.class);
    }

    public TabPaneModel tabPane() {
        return create(TabPaneModel.class);
    }

    public TabPaneItemModel tabPaneItem(final ItemModel content, final String caption) {
        final TabPaneItemModel item = create(TabPaneItemModel.class);
        item.setContent(content);
        item.setCaption(caption);
        return item;
    }
}
