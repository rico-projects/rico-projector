package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import org.controlsfx.control.ListSelectionView;
import to.remove.ui.listselectionview.ListSelectionViewItemModel;
import to.remove.ui.listselectionview.ListSelectionViewModel;

public class ListSelectionViewFactory implements ProjectorNodeFactory<ListSelectionViewModel, ListSelectionView<ListSelectionViewItemModel>> {


    @Override
    public ListSelectionView<ListSelectionViewItemModel> create(final Projector projector, final ListSelectionViewModel model) {
        return new ListSelectionView<>();
    }

    @Override
    public Class<ListSelectionViewModel> getSupportedType() {
        return ListSelectionViewModel.class;
    }
}
