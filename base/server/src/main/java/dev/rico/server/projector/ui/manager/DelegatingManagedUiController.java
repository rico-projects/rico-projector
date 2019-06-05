package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingContext;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class DelegatingManagedUiController implements ManagedUiController {
    private ManagedUiController delegate;
    private Retained<ItemModel> thisUi;

    public DelegatingManagedUiController(ManagedUiController delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    protected DelegatingManagedUiController() {
    }

    public void setDelegate(ManagedUiController delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    public ItemModel getUi() {
        if (thisUi == null) {
            thisUi = retain(buildUi());
        }
        return thisUi.get();
    }

    @Override
    public ServerUiManager ui() {
        return delegate.ui();
    }

    @Override
    public RemotingContext getSession() {
        return delegate.getSession();
    }

    @Override
    public BeanManager getBeanManager() {
        return delegate.getBeanManager();
    }

    @Override
    public ManagedUiModel getModel() {
        return delegate.getModel();
    }

    protected <T> void doInParallel(Supplier<T> backgroundJob, Consumer<T> refreshJob, Consumer<Exception> errorJob) {
        new Thread(() -> {
            try {
                T obj = backgroundJob.get();
                synchronized (obj) {
                    getSession().createSessionExecutor().runLaterInClientSession(() -> {
                        synchronized (obj) {
                            refreshJob.accept(obj);
                        }
                    });
                }
            } catch (Exception exception) {
                synchronized (exception) {
                    getSession().createSessionExecutor().runLaterInClientSession(() -> {
                        synchronized (exception) {
                            errorJob.accept(exception);
                        }
                    });
                }
            }
        }
        ).start();
    }

    protected <V> V callInUi(Callable<V> callInUiThread) {
        try {
            return getSession().createSessionExecutor().callLaterInClientSession(callInUiThread).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runInUi(Runnable runInUiThread) {
        try {
            getSession().createSessionExecutor().runLaterInClientSession(runInUiThread).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void runAndForgetInUi(Runnable runInUiThread) {
        getSession().createSessionExecutor().runLaterInClientSession(runInUiThread).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
