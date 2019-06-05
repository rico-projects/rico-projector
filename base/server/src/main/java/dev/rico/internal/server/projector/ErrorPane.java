package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.BorderPaneModel;
import dev.rico.internal.projector.ui.LabelModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.internal.projector.ui.box.VBoxModel;
import dev.rico.server.remoting.RemotingContext;
import javafx.geometry.Pos;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class ErrorPane {
   private final ManagedUiController delegate;

   public ErrorPane(ManagedUiController delegate) {
      this(delegate, "Error :-(", null);
   }

   public ErrorPane(ManagedUiController delegate, String message, Exception exception) {
      this.delegate = Objects.requireNonNull(delegate);
      BorderPaneModel borderPane = ui().borderPane();
      VBoxModel centerBox = ui().vBox();
      centerBox.setAlignment(Pos.CENTER);
      centerBox.setSpacing(14);
      borderPane.setCenter(centerBox);
      LabelModel errorLabel = ui().label(message);
      centerBox.add(ui().vBoxItem(errorLabel));
      getModel().setRoot(borderPane);
      if (exception != null) {
         showException(centerBox, exception);
      }
   }

   private ServerUiManager ui() {
      return delegate.ui();
   }

   private void showException(VBoxModel centerBox, Exception exception) {
      StringWriter message = new StringWriter();
      exception.printStackTrace(new PrintWriter(message));
      centerBox.add(ui().vBoxItem(ui().label(exception.getMessage())));
      TextAreaModel content = ui().textArea();
      content.setWrapText(false);
      content.setText(message.toString());
      centerBox.add(ui().vBoxItem(content));
   }

   public ErrorPane(ManagedUiController delegate, String message) {
      this(delegate, message, null);
   }

   public ErrorPane(ManagedUiController delegate, Exception exception) {
      this(delegate, "Error :-(", exception);
   }

   private ManagedUiModel getModel() {
      return delegate.getModel();
   }

   private RemotingContext getSession() {
      return delegate.getSession();
   }
}
