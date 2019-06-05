package dev.rico.internal.projector.ui.dialog;


import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class DialogModel extends ItemModel {
   private Property<ItemModel> owner;
   private Property<String> title;

   public ItemModel getOwner() {
      return owner.get();
   }

   public void setOwner(ItemModel owner) {
      this.owner.set(owner);
   }

   public Property<ItemModel> ownerProperty() {
      return owner;
   }

   public String getTitle() {
      return title.get();
   }

   public void setTitle(String title) {
      this.title.set(title);
   }

   public Property<String> titleProperty() {
      return title;
   }
}
