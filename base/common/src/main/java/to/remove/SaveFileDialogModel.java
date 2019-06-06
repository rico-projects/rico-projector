package to.remove;


import dev.rico.internal.projector.ui.dialog.DialogModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class SaveFileDialogModel extends DialogModel {
   private Property<String> directory;
   private Property<String> fileName;
   private Property<DocumentData> saveThis;

   public DocumentData getSaveThis() {
      return saveThis.get();
   }

   public void setSaveThis(DocumentData saveThis) {
      this.saveThis.set(saveThis);
   }

   public Property<DocumentData> saveThisProperty() {
      return saveThis;
   }

   public String getFileName() {
      return fileName.get();
   }

   public void setFileName(String fileName) {
      this.fileName.set(fileName);
   }

   public Property<String> fileNameProperty() {
      return fileName;
   }

   public String getDirectory() {
      return directory.get();
   }

   public void setDirectory(String directory) {
      this.directory.set(directory);
   }

   public Property<String> directoryProperty() {
      return directory;
   }
}
