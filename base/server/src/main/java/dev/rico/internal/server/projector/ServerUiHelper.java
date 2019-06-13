package dev.rico.internal.server.projector;

import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.DateTimeFieldModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.internal.projector.ui.WithPromptText;
import dev.rico.internal.projector.ui.box.HBoxItemModel;
import dev.rico.internal.projector.ui.box.HBoxModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.internal.projector.ui.table.TableModel;
import dev.rico.internal.projector.ui.table.TableRowModel;
import javafx.geometry.HPos;
import javafx.scene.layout.Priority;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

import static java.time.LocalDateTime.of;

public class ServerUiHelper {
    public static Instant get(DateTimeFieldModel startTime) {
        return Optional.ofNullable(startTime)
                .map(dateTimeFieldModel -> dateTimeFieldModel.getDate() != null && dateTimeFieldModel.getTime() != null ? of(dateTimeFieldModel.getDate(), dateTimeFieldModel.getTime()).toInstant(ZoneOffset.UTC) : null)
                .orElse(null);
    }

    public static String get(TextFieldModel textFieldModel) {
        return Optional.ofNullable(textFieldModel)
                .map(TextFieldModel::getText)
                .orElse(null);
    }

    public static HBoxModel wrapInHorizontalBox(ServerUiManager ui, ItemModel... items) {
        Assert.requireNonNull(items, "items");
        HBoxModel hBox = ui.hBox();
        Arrays.stream(items).forEach(itemModel -> hBox.add(ui.hBoxItem(itemModel)));
        return hBox;
    }

    public static GridPaneItemModel hGrowSometimes(GridPaneItemModel gridPaneItemModel) {
        gridPaneItemModel.sethGrow(Priority.SOMETIMES);
        return gridPaneItemModel;
    }

    public static GridPaneItemModel alignRight(GridPaneItemModel gridPaneItemModel) {
        gridPaneItemModel.sethAlignment(HPos.RIGHT);
        gridPaneItemModel.sethGrow(Priority.NEVER);
        return gridPaneItemModel;
    }

    public static HBoxItemModel hGrowSometimes(HBoxItemModel model) {
        model.sethGrow(Priority.SOMETIMES);
        return model;
    }

    public static HBoxItemModel hGrowAlways(HBoxItemModel model) {
        model.sethGrow(Priority.ALWAYS);
        return model;
    }

    public static HBoxItemModel hGrowNever(HBoxItemModel model) {
        model.sethGrow(Priority.NEVER);
        return model;
    }

    public static <T extends WithPromptText> T promptText(T model, String prompt) {
        model.setPromptText(prompt);
        return model;
    }

    public static void selectByReference(ChoiceBoxModel choiceBox, String reference) {
        if (reference == null) {
            choiceBox.setSelected(null);
        } else {
            for (ChoiceBoxItemModel choiceBoxItemModel : choiceBox.getItems()) {
                if (choiceBoxItemModel.getReference().equals(reference)) {
                    choiceBox.setSelected(choiceBoxItemModel);
                    return;
                }
            }
            throw new IllegalArgumentException("Unknown reference: " + reference);
        }
    }

    /**
     * @return true, wenn etwas getan wurde, false ansonsten.
     */
    public static boolean maySelectByReference(ChoiceBoxModel choiceBox, String reference) {
        if (reference == null) {
            choiceBox.setSelected(null);
            return true;
        } else {
            for (ChoiceBoxItemModel choiceBoxItemModel : choiceBox.getItems()) {
                if (choiceBoxItemModel.getReference().equals(reference)) {
                    choiceBox.setSelected(choiceBoxItemModel);
                    return true;
                }
            }
        }
        return false;
    }

    public static void selectByReference(TableModel table, String reference) {
        if (reference == null) {
            table.setSelectedRow(null);
        } else {
            for (TableRowModel row : table.getRows()) {
                if (row.getReference().equals(reference)) {
                    table.setSelectedRow(row);
                    return;
                }
            }
            throw new IllegalArgumentException("Unknown reference: " + reference);
        }
    }

    public static void selectFirstItem(ChoiceBoxModel box) {
        box.setSelected(box.getItems().stream().findAny().orElse(null));
    }

    public static Optional<TableRowModel> findRowInTable(TableModel table, String reference) {
        return table.getRows().stream().filter(tableRowModel -> tableRowModel.getReference().equals(reference)).findAny();
    }

}
