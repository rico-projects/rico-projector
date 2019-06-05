package dev.rico.client.projector.uimanager;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UnexpectedErrorDialog extends Alert {

   private final TextArea textArea;
   private final ButtonType shutDownButton;
   private boolean lethalCause;

   public UnexpectedErrorDialog() {
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

      shutDownButton = new ButtonType("App sofort beenden");

      getDialogPane().setExpandableContent(expContent);

      errorTextProperty().addListener(this::onErrorTextChange);

      setOnHidden(event -> {
         if (lethalCause || getResult() == shutDownButton) {
            System.exit(0);
         }
      });
   }

   static public Void showError(Node owner, Throwable exception) {
      UnexpectedErrorDialog unexpectedErrorDialog = new UnexpectedErrorDialog();
      if (owner != null && owner.getScene() != null && owner.getScene().getWindow() != null) {
         unexpectedErrorDialog.initOwner(owner.getScene().getWindow());
      }
      unexpectedErrorDialog.setStackTrace(exception);
      unexpectedErrorDialog.showAndWait();
      return null;
   }

   final public StringProperty errorTextProperty() {
      return textArea.textProperty();
   }

   public void setStackTrace(Throwable stackTrace) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      stackTrace.printStackTrace(pw);
      String exceptionText = sw.toString();
      errorTextProperty().setValue(exceptionText);
   }

   private void onErrorTextChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
      lethalCause = newValue.startsWith("java.lang.IllegalStateException: Connection is broken");
      if (lethalCause) {
         configureAsLethal();
      } else {
         configureAsRecoverable();
      }
   }

   private void configureAsLethal() {
      getButtonTypes().remove(shutDownButton);
      setTitle("Es ist ein unbehebbarer Fehler aufgetreten");
      setHeaderText("In sprouts fly office ist leider ein schwerer Fehler aufgetreten. Die Software beendet sich nun aus\n" +
            "technischen Gründen. Sie müssen sprouts fly office neu starten und sich dann erneut einloggen.");
      setContentText("Diese Meldung deutet auf einen Fehler im Quelltext von fly office hin.\n\nSie können uns bei der Behebung des Fehlers unterstützen, indem Sie auf 'Details anzeigen' klicken,\n" +
            "die angezeigten Informationen mit Rechtsklick, 'Alles markieren', dann 'Kopieren' und in eine Email\n" +
            "einfügen, die Sie an mailto:techsupport@sprouts.aero schicken.\n" +
            "\n" +
            "Vielen Dank!\n" +
            "\n" +
            "- Ihr Software-Team bei sprouts");
   }

   private void configureAsRecoverable() {
      getButtonTypes().add(0, shutDownButton);
      setTitle("Es ist ein schwerer Fehler aufgetreten");
      setHeaderText("In sprouts fly office ist leider ein Fehler aufgetreten. " +
            "Sie müssen die Software\nmöglicherweise beenden und neu starten, falls sie nicht mehr funktioniert.");
      setContentText("Diese Meldung deutet auf einen Fehler im Quelltext von fly office hin.\n\nSie können uns bei der Behebung des Fehlers unterstützen, indem Sie auf 'Details anzeigen' klicken,\n" +
            "die angezeigten Informationen mit Rechtsklick, 'Alles markieren', dann 'Kopieren' und in eine Email\n" +
            "einfügen, die Sie an techsupport@sprouts.aero schicken.\n\nVielen Dank!\n\n- Ihr Software-Team bei sprouts");
   }
}
