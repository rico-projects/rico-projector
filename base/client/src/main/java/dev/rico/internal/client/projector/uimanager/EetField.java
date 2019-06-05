package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.client.remoting.ControllerProxy;
import javafx.scene.control.TextFormatter;

@ForRemoval
public class EetField extends TextField {
    public EetField(ControllerProxy<?> controllerProxy, TextFieldModel model) {
        super(controllerProxy, model);
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*") && text.length() <= 4) {
                return change;
            }
            return null;
        });
        setTextFormatter(textFormatter);
    }
}
