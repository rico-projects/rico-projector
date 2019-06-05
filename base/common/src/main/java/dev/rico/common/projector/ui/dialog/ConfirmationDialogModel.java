package dev.rico.common.projector.ui.dialog;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ConfirmationDialogModel extends DialogModel {
   private Property<String> headerText;
   private Property<String> contentText;
   private Property<String> okayAction;

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

   public String getOkayAction() {
      return okayAction.get();
   }

   public void setOkayAction(String okayAction) {
      this.okayAction.set(okayAction);
   }

   public Property<String> okayActionProperty() {
      return okayAction;
   }
}
