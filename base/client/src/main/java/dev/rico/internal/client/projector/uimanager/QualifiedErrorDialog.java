package dev.rico.internal.client.projector.uimanager;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class QualifiedErrorDialog extends Alert {

   private final TextArea textArea;

   public QualifiedErrorDialog() {
      super(AlertType.ERROR);

      Label label = new Label("Der Exception-Stacktrace lautet:");

      textArea = new TextArea();
      textArea.setEditable(false);
      textArea.setWrapText(true);

      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      expContent.add(label, 0, 0);
      expContent.add(textArea, 0, 1);

      getDialogPane().setExpandableContent(expContent);
   }

   static public Void showError(Node owner, Throwable exception) {
      QualifiedErrorDialog dialog = new QualifiedErrorDialog();
      if (owner != null && owner.getScene() != null && owner.getScene().getWindow() != null) {
         dialog.initOwner(owner.getScene().getWindow());
      }
      dialog.setStackTrace(exception);
      dialog.showAndWait();
      return null;
   }

   public void setStackTrace(Throwable stackTrace) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      stackTrace.printStackTrace(pw);
      String exceptionText = sw.toString();
      errorTextProperty().setValue(exceptionText);
   }

   final public StringProperty errorTextProperty() {
      return textArea.textProperty();
   }

   public void setRootCauseText(String rootCauseText) {
      if (rootCauseText != null && !rootCauseText.trim().isEmpty()) {
         setContentText(getContentText() + "\n\nMögliche Ursache: " + rootCauseText);
      }
   }
}
