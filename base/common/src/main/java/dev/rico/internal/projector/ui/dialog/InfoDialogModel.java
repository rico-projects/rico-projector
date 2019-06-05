package dev.rico.internal.projector.ui.dialog;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class InfoDialogModel extends DialogModel {
   private Property<String> headerText;
   private Property<String> contentText;

   public String getHeaderText() {
      return headerText.get();
   }

   public void setHeaderText(String headerText) {
      this.headerText.set(headerText);
   }

   public Property<String> headerTextProperty() {
      return headerText;
   }

   public String getContentText() {
      return contentText.get();
   }

   public void setContentText(String contentText) {
      this.contentText.set(contentText);
   }

   public Property<String> contentTextProperty() {
      return contentText;
   }
}
