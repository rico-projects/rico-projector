package dev.rico.common.projector.ui.table;

import dev.rico.common.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.remoting.Property;

public class TableChoiceBoxCellModel extends TableCellModel<ChoiceBoxItemModel> {
   private Property<ChoiceBoxItemModel> value;

   public Property<ChoiceBoxItemModel> valueProperty() {
      return value;
   }
}
