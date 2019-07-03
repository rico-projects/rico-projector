package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.dialog.QualifiedErrorDialogModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.ClientSessionExecutor;
import dev.rico.server.remoting.Param;
import dev.rico.server.remoting.RemotingAction;
import dev.rico.server.remoting.RemotingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class AbstractManagedUiController implements ManagedUiController {

    private final ClientSessionExecutor sessionExecutor;
    private final BeanManager beanManager;
    private final RemotingContext session;
    private ServerUiManager uiManager;

    public AbstractManagedUiController(final BeanManager beanManager, final RemotingContext session) {
        this.beanManager = beanManager;
        this.session = session;
        sessionExecutor = session.createSessionExecutor();
    }

    protected <V> V callInUi(final Callable<V> callInUiThread) {
        try {
            return sessionExecutor.callLaterInClientSession(callInUiThread).get();
        } catch (final InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runInUi(final Runnable runInUiThread) {
        try {
            sessionExecutor.runLaterInClientSession(runInUiThread).get();
        } catch (final InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runAndForgetInUi(final Runnable runInUiThread) {
        sessionExecutor.runLaterInClientSession(runInUiThread).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @PostConstruct
    protected void init() {
        try {
            postConstruct();
            uiManager = createUiManager();
            initModel(getModel());
            getModel().setRoot(buildUi());
            initRootPane();
        } catch (final Exception e) {
            showUnexpectedError(e);
        }
    }

    protected ServerUiManager createUiManager() {
        return new ServerUiManager(beanManager);
    }

    protected void postConstruct() {

    }

    protected void initModel(final ManagedUiModel model) {

    }

    protected void initRootPane() {

    }

    protected void showUnexpectedError(final Throwable stackTrace) {
        getModel().setIsWorking(false);
        stackTrace.printStackTrace();
        final String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        getModel().showDialog(ui().unexpectedErrorDialog(getModel().getRoot(), exceptionText));
    }

    protected void showQualifiedError(final String headerText, final String contentText, final Throwable stackTrace) {
        getModel().setIsWorking(false);
        stackTrace.printStackTrace();
        final String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        final QualifiedErrorDialogModel dialog = ui().qualifiedErrorDialog(getModel().getRoot(), headerText, contentText, exceptionText);
        dialog.setRootCauseText(ExceptionUtils.getRootCauseMessage(stackTrace));
        getModel().showDialog(dialog);
    }

    @Override
    public ServerUiManager ui() {
        return uiManager;
    }

    @Override
    public RemotingContext getSession() {
        return session;
    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }

    public abstract ManagedUiModel getModel();

    @RemotingAction
    public void receivedFocus(@Param("id") final String id) {
        processReceivedFocus(id);
    }

    protected void processReceivedFocus(final String fieldId) {
    }
}
