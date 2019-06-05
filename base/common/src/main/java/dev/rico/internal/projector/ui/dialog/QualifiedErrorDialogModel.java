package dev.rico.internal.projector.ui.dialog;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class QualifiedErrorDialogModel extends DialogModel {
   private Property<String> headerText;
   private Property<String> contentText;
   private Property<String> exceptionText;
   private Property<String> rootCauseText;

   public String getExceptionText() {
      return exceptionText.get();
   }

   public void setExceptionText(String exceptionText) {
      this.exceptionText.set(exceptionText);
   }

   public Property<String> exceptionTextProperty() {
      return exceptionText;
   }

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

   public String getRootCauseText() {
      return rootCauseText.get();
   }

   public void setRootCauseText(String rootCauseText) {
      this.rootCauseText.set(rootCauseText);
   }

   public Property<String> rootCauseTextProperty() {
      return rootCauseText;
   }
}
