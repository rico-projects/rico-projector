package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.Client;
import dev.rico.client.concurrent.BackgroundExecutor;
import dev.rico.client.concurrent.UiExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncSequence {
    private static ArrayBlockingQueue<CompletableFuture<Object>> queue;
    private static Runnable job;

    public static <T> void doAsync(final Supplier<T> asyncJob, final Consumer<T> onSuccess, final Consumer<Exception> onError) {
        try {
            getQueue().put(CompletableFuture.supplyAsync(asyncJob::get,
                  Client.getService(BackgroundExecutor.class)));
        } catch (InterruptedException ignored) {
        }

        if (job == null) {
            job = () -> {
                CompletableFuture<T> peek = (CompletableFuture<T>) getQueue().peek();
                if (peek != null && peek.isDone()) {
                    try {
                        T documentData = (T) getQueue().take().get();
                        Client.getService(UiExecutor.class).execute(() -> {
                            onSuccess.accept(documentData);
                            if (job != null) {
                                Client.getService(UiExecutor.class).execute(job);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (peek != null && job != null) {
                    Client.getService(UiExecutor.class).execute(job);
                } else {
                    job = null;
                    queue = null;
                }
            };
        }

        job.run();
    }

    private static ArrayBlockingQueue<CompletableFuture<Object>> getQueue() {
        if (queue == null) {
            queue = new ArrayBlockingQueue<>(100, true);
        }
        return queue;
    }
}
