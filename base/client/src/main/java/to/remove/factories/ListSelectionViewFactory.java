package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import org.controlsfx.control.ListSelectionView;
import to.remove.ui.listselectionview.ListSelectionViewItemModel;
import to.remove.ui.listselectionview.ListSelectionViewModel;

public class ListSelectionViewFactory implements ProjectorNodeFactory<ListSelectionViewModel, ListSelectionView<ListSelectionViewItemModel>> {


    @Override
    public ListSelectionView<ListSelectionViewItemModel> create(Projector projector, ListSelectionViewModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        return new ListSelectionView<>();
    }

    @Override
    public Class<ListSelectionViewModel> getSupportedType() {
        return ListSelectionViewModel.class;
    }
}
