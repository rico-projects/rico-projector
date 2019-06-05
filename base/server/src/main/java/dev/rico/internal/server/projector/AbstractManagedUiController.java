package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.ToggleButtonModel;
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

    public AbstractManagedUiController(BeanManager beanManager, RemotingContext session) {
        this.beanManager = beanManager;
        this.session = session;
        sessionExecutor = session.createSessionExecutor();
    }

    protected <V> V callInUi(Callable<V> callInUiThread) {
        try {
            return sessionExecutor.callLaterInClientSession(callInUiThread).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runInUi(Runnable runInUiThread) {
        try {
            sessionExecutor.runLaterInClientSession(runInUiThread).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runAndForgetInUi(Runnable runInUiThread) {
        sessionExecutor.runLaterInClientSession(runInUiThread).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @PostConstruct
    protected void init() {
        try {
            postConstruct();
            uiManager = new ServerUiManager(getModel(), beanManager);
            initModel(getModel());
            getModel().setRoot(buildUi());
            initRootPane();
        } catch (Exception e) {
            showUnexpectedError(e);
        }
    }

    protected void postConstruct() {

    }

    protected void initModel(ManagedUiModel model) {

    }

    protected void initRootPane() {

    }

    //    TODO: @ActionExceptionHandler
    protected void showUnexpectedError(Throwable stackTrace) {
        getModel().setIsWorking(false);
        stackTrace.printStackTrace();
        String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        getModel().showDialog(ui().unexpectedErrorDialog(getModel().getRoot(), exceptionText));
    }

    //    TODO: @ActionExceptionHandler
    protected void showQualifiedError(String headerText, String contentText, Throwable stackTrace) {
        getModel().setIsWorking(false);
        stackTrace.printStackTrace();
        String exceptionText = ExceptionUtils.getStackTrace(stackTrace);
        QualifiedErrorDialogModel dialog = ui().qualifiedErrorDialog(getModel().getRoot(), headerText, contentText, exceptionText);
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

    @RemotingAction("buttonClick")
    public void event(@Param("button") IdentifiableModel button) {
        uiManager.receivedButtonClick(button);
    }


    @RemotingAction
    private void onToggleButtonAction(@Param("model") ToggleButtonModel item, @Param("selected") Boolean selected) {

    }



    @RemotingAction
    public void receivedFocus(@Param("id") String id) {
        processReceivedFocus(id);
    }

    protected void processReceivedFocus(String fieldId) {
    }
}
