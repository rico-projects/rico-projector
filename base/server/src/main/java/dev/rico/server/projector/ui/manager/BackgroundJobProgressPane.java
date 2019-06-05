package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.BorderPaneModel;
import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.ProgressIndicatorModel;
import dev.rico.common.projector.ui.box.VBoxModel;
import dev.rico.server.remoting.ClientSessionExecutor;
import javafx.geometry.Pos;

public abstract class BackgroundJobProgressPane extends UiPane {

   private final Object lockObject = new Object();

   protected BackgroundJobProgressPane(ManagedUiController delegate) {
      super(delegate);
      getModel().setRoot(getPane());
   }

   @Override
   public ItemModel buildUi() {
      BorderPaneModel borderPane = ui().borderPane();
      VBoxModel centerBox = ui().vBox();
      centerBox.setAlignment(Pos.CENTER);
      centerBox.setSpacing(14);
      borderPane.setCenter(centerBox);
      ProgressIndicatorModel waitIndicator = ui().progressIndicator();
      centerBox.add(ui().vBoxItem(waitIndicator));
      ClientSessionExecutor sessionExecutor = getSession().createSessionExecutor();
      synchronized(lockObject) {
         new Thread(() -> {
            synchronized(lockObject) {
               try {
                  doJob();
                  sessionExecutor.runLaterInClientSession(this::onFinish);
               } catch (Exception e) {
                  sessionExecutor.runLaterInClientSession(() -> onError(e));
               }
            }
         }
         ).start();
      }
      return borderPane;
   }

   abstract protected void doJob() throws Exception;

   protected void onError(Exception e) {
      new ErrorPane(getDelegate(), e);
   }

   protected void onFinish() {
      new JobFinishedPane(getDelegate());
   }
}
