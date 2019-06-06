package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.projector.ui.dialog.SaveFileDialogModel;
import dev.rico.internal.projector.ui.dialog.UnexpectedErrorDialogModel;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SaveFileDialogHandler implements ProjectorDialogHandler<SaveFileDialogModel>, DialogConfiguration  {

    @Override
    public void show(final Projector projector, final SaveFileDialogModel model) {
        FileChooser chooser = new FileChooser();
        if (model.getDirectory() != null) {
            File folder = new File(model.getDirectory());
            if (!convertFolder(folder).isEmpty()) {
                chooser.setInitialDirectory(folder);
            }
        }
        chooser.setInitialFileName(model.getFileName());
        File saveFile = chooser.showSaveDialog(findWindowOptional(projector, model).orElse(null));
        if (saveFile != null && !saveFile.isDirectory()) {
            try {
                FileUtils.writeByteArrayToFile(saveFile, model.getSaveThis().getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Class<SaveFileDialogModel> getSupportedType() {
        return SaveFileDialogModel.class;
    }

    private static String convertFolder(File folder) {
        if (folder != null) {
            if (folder.isDirectory()) {
                try {
                    return folder.getCanonicalPath();
                } catch (IOException e) {
                    return "";
                }
            } else {
                return "";
            }
        }
        return "";
    }

}
