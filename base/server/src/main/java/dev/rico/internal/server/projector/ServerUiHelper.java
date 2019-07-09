package dev.rico.internal.server.projector;

import static java.time.LocalDateTime.of;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

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

public class ServerUiHelper {

    public static Instant get(final DateTimeFieldModel startTime) {
        return Optional.ofNullable(startTime)
                .map(dateTimeFieldModel -> dateTimeFieldModel.getDate() != null && dateTimeFieldModel.getTime() != null ? of(dateTimeFieldModel.getDate(), dateTimeFieldModel.getTime()).toInstant(ZoneOffset.UTC) : null)
                .orElse(null);
    }

    public static String get(final TextFieldModel textFieldModel) {
        return Optional.ofNullable(textFieldModel)
                .map(TextFieldModel::getText)
                .orElse(null);
    }

    public static HBoxModel wrapInHorizontalBox(final ServerUiManager ui, final ItemModel... items) {
        Assert.requireNonNull(items, "items");

        final HBoxModel hBox = ui.hBox();
        Arrays.stream(items).forEach(itemModel -> hBox.add(ui.hBoxItem(itemModel)));
        return hBox;
    }

    public static GridPaneItemModel hGrowSometimes(final GridPaneItemModel gridPaneItemModel) {
        Assert.requireNonNull(gridPaneItemModel, "gridPaneItemModel");

        gridPaneItemModel.sethGrow(Priority.SOMETIMES);
        return gridPaneItemModel;
    }

    public static GridPaneItemModel alignRight(final GridPaneItemModel gridPaneItemModel) {
        Assert.requireNonNull(gridPaneItemModel, "gridPaneItemModel");

        gridPaneItemModel.sethAlignment(HPos.RIGHT);
        gridPaneItemModel.sethGrow(Priority.NEVER);
        return gridPaneItemModel;
    }

    public static HBoxItemModel hGrowSometimes(final HBoxItemModel model) {
        Assert.requireNonNull(model, "model");

        model.sethGrow(Priority.SOMETIMES);
        return model;
    }

    public static HBoxItemModel hGrowAlways(final HBoxItemModel model) {
        Assert.requireNonNull(model, "model");

        model.sethGrow(Priority.ALWAYS);
        return model;
    }

    public static HBoxItemModel hGrowNever(final HBoxItemModel model) {
        Assert.requireNonNull(model, "model");

        model.sethGrow(Priority.NEVER);
        return model;
    }

    public static <T extends WithPromptText> T promptText(final T model, final String prompt) {
        Assert.requireNonNull(model, "model");

        model.setPromptText(prompt);
        return model;
    }

    public static void selectByReference(final ChoiceBoxModel choiceBox, final String reference) {
        Assert.requireNonNull(choiceBox, "choiceBox");

        if (reference == null) {
            choiceBox.setSelected(null);
        } else {
            for (final ChoiceBoxItemModel choiceBoxItemModel : choiceBox.getItems()) {
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
    public static boolean maySelectByReference(final ChoiceBoxModel choiceBox, final String reference) {
        Assert.requireNonNull(choiceBox, "choiceBox");

        if (reference == null) {
            choiceBox.setSelected(null);
            return true;
        } else {
            for (final ChoiceBoxItemModel choiceBoxItemModel : choiceBox.getItems()) {
                if (choiceBoxItemModel.getReference().equals(reference)) {
                    choiceBox.setSelected(choiceBoxItemModel);
                    return true;
                }
            }
        }
        return false;
    }

    public static void selectByReference(final TableModel table, final String reference) {
        Assert.requireNonNull(table, "table");

        if (reference == null) {
            table.setSelectedRow(null);
        } else {
            for (final TableRowModel row : table.getRows()) {
                if (row.getReference().equals(reference)) {
                    table.setSelectedRow(row);
                    return;
                }
            }
            throw new IllegalArgumentException("Unknown reference: " + reference);
        }
    }

    public static void selectFirstItem(final ChoiceBoxModel box) {
        Assert.requireNonNull(box, "box");

        box.setSelected(box.getItems().stream().findAny().orElse(null));
    }

    public static Optional<TableRowModel> findRowInTable(final TableModel table, final String reference) {
        Assert.requireNonNull(table, "table");

        return table.getRows().stream().filter(tableRowModel -> tableRowModel.getReference().equals(reference)).findAny();
    }
}
