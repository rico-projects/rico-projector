package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import to.remove.components.ClientPropertiesTable;
import to.remove.ui.ClientPropertiesTableModel;

public class ClientPropertiesTableFactory implements ProjectorNodeFactory<ClientPropertiesTableModel, ClientPropertiesTable> {

    @Override
    public ClientPropertiesTable create(final Projector projector, final ClientPropertiesTableModel model) {
        return new ClientPropertiesTable();
    }

    @Override
    public Class<ClientPropertiesTableModel> getSupportedType() {
        return ClientPropertiesTableModel.class;
    }
}
