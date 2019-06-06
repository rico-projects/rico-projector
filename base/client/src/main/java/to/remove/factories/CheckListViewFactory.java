package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import to.remove.ui.checklistview.CheckListViewModel;
import to.remove.uimanager.CheckListView;

public class CheckListViewFactory implements ProjectorNodeFactory<CheckListViewModel, CheckListView> {

    @Override
    public CheckListView create(final Projector projector, final CheckListViewModel model) {
        return new CheckListView(model);
    }

    @Override
    public Class<CheckListViewModel> getSupportedType() {
        return CheckListViewModel.class;
    }
}
