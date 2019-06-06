package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.DateTimeFieldModel;
import to.remove.uimanager.DateTimeField;

public class DateTimeFieldFactory implements ProjectorNodeFactory<DateTimeFieldModel, DateTimeField> {
    @Override
    public DateTimeField create(Projector projector, DateTimeFieldModel model) {
        return new DateTimeField(model);
    }

    @Override
    public Class<DateTimeFieldModel> getSupportedType() {
        return DateTimeFieldModel.class;
    }
}
