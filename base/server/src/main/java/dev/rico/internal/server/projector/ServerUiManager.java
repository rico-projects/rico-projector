package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.*;
import dev.rico.internal.projector.ui.box.HBoxItemModel;
import dev.rico.internal.projector.ui.box.HBoxModel;
import dev.rico.internal.projector.ui.box.VBoxItemModel;
import dev.rico.internal.projector.ui.box.VBoxModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.internal.projector.ui.dialog.*;
import dev.rico.internal.projector.ui.flowpane.FlowPaneItemModel;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import dev.rico.internal.projector.ui.listview.ListViewItemModel;
import dev.rico.internal.projector.ui.listview.ListViewModel;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneItemModel;
import dev.rico.internal.projector.ui.splitpane.SplitPaneModel;
import dev.rico.internal.projector.ui.table.*;
import dev.rico.internal.projector.ui.tabpane.TabPaneItemModel;
import dev.rico.internal.projector.ui.tabpane.TabPaneModel;
import dev.rico.remoting.BeanManager;
import javafx.geometry.Orientation;
import javafx.scene.layout.Priority;

import java.time.Instant;
import java.util.Arrays;

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

    public TextFieldModel textField() {
        return create(TextFieldModel.class);
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

    public SplitPaneItemModel splitPaneItem(final ItemModel content, final double dividerPosition) {
        final SplitPaneItemModel model = create(SplitPaneItemModel.class);
        model.setContent(content);
        model.setDividerPosition(dividerPosition);
        return model;
    }

    public TextAreaModel textArea() {
        return create(TextAreaModel.class);
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

    public DateTimeFieldModel dateTimeField() {
        return create(DateTimeFieldModel.class);
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

    public ShutdownDialogModel shutdownDialog() {
        return create(ShutdownDialogModel.class);
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

    public ButtonModel button() {
        return create(ButtonModel.class);
    }

    public CustomDialogModel customDialog(final String title) {
        final CustomDialogModel model = create(CustomDialogModel.class);
        model.setTitle(title);
        return model;
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
