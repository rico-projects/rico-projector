package dev.rico.common.projector.ui.table;

import dev.rico.common.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class TableChoiceBoxColumnModel extends TableColumnModel {
   private ObservableList<ChoiceBoxItemModel> items;

   public ObservableList<ChoiceBoxItemModel> getItems() {
      return items;
   }
}
