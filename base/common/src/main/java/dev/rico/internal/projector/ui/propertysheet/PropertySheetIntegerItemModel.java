package dev.rico.internal.projector.ui.propertysheet;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
@ForRemoval
public class PropertySheetIntegerItemModel extends PropertySheetItemModel {
   private Property<Integer> value;

   public Property<Integer> valueProperty() {
      return value;
   }

   public Integer getValue() {
      return value.get();
   }

   public void setValue(Integer value) {
      this.value.set(value);
   }
}
