package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.view.AbstractViewController;
import dev.rico.internal.client.projector.mixed.ClientContextHolder;
import dev.rico.internal.client.projector.uimanager.presenter.ViewPresenter;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class ManagedUiViewController<M extends ManagedUiModel> extends AbstractViewController<M> implements ViewPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedUiViewController.class);

    private final BorderPane pane = new BorderPane();
    private ClientUiManager factory;

    public ManagedUiViewController(final String controllerName) {
        super(ClientContextHolder.getContext(), controllerName);
    }

    @Override
    protected void init() {
        try {
            FXBinder.bind(isWorkingProperty()).to(getModel().isWorkingProperty());
            addCSSIfAvailable(pane);
            factory = new ClientUiManager(getControllerProxy(), newPostProcessor());
            installEventHandler();
            pane.centerProperty().bind(factory.rootProperty());
        } catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void installEventHandler() {
//        factory.setHandler(event -> {
//            if (event instanceof CreateNewDocumentViewEvent) {
//                CreateNewDocumentViewEvent createNewDocumentViewEvent = (CreateNewDocumentViewEvent) event;
//                CreateDocumentPresenter presenter = new CreateDocumentPresenter(createNewDocumentViewEvent.getTemplate(), createNewDocumentViewEvent.getTripId(),
//                        () -> {
//                        });
//                ApplicationHolder.getApp().newTab("Dokument erzeugen", presenter);
//            } else if (event instanceof CreateHistoryViewEvent) {
//                CreateHistoryViewEvent createHistoryViewEvent = (CreateHistoryViewEvent) event;
//                ViewPresenter presenter = new HistoryViewPresenter(createHistoryViewEvent);
//                ApplicationHolder.getApp().newTab("Änderungshistorie", presenter);
//            } else if (event instanceof CreateNewTripEvent) {
//                CreateNewTripEvent createNewTripEvent = (CreateNewTripEvent) event;
//                ViewPresenter presenter = new CreateTripPresenter(createNewTripEvent);
//                ApplicationHolder.getApp().newTab("Neuer Umlauf aus Anfrage", presenter);
//            } else if (event instanceof CreateNewFlightEvent) {
//                CreateNewFlightEvent createNewFlightEvent = (CreateNewFlightEvent) event;
//                ViewPresenter presenter = new TripPresenter(createNewFlightEvent);
//                ApplicationHolder.getApp().newTab("Neuer Flug aus Anfrage", presenter);
//            } else if (event != null) {
//                throw new IllegalArgumentException("Unknown event of type " + event.getClass());
//            }
//        });
    }

    private void addCSSIfAvailable(final Parent parent) {
        final URL uri = this.getClass().getResource(this.getStyleSheetName());
        if (uri != null) {
            final String uriToCss = uri.toExternalForm();
            parent.getStylesheets().add(uriToCss);
        }
    }

    protected PostProcessor newPostProcessor() {
        return (id, model, node) -> {

        };
    }

    private String getStyleSheetName() {
        return this.getResourceCamelOrLowerCase(false, ".css");
    }

    private String getResourceCamelOrLowerCase(final boolean mandatory, final String ending) {
        String name = this.getConventionalName(true, ending);
        URL found = this.getClass().getResource(name);
        if (found != null) {
            return name;
        } else {
            LOGGER.error("File: {} not found, attempting with camel case", name);
            name = this.getConventionalName(false, ending);
            found = this.getClass().getResource(name);
            if (mandatory && found == null) {
                final String message = "Cannot load file " + name;
                LOGGER.error(message);
                LOGGER.error("Stopping initialization phase...");
                throw new IllegalStateException(message);
            } else {
                return name;
            }
        }
    }

    private String getConventionalName(final boolean lowercase, final String ending) {
        return this.getConventionalName(lowercase) + ending;
    }

    private String getConventionalName(final boolean lowercase) {
        final String clazzWithEnding = this.getClass().getSimpleName();
        String clazz = stripEnding(clazzWithEnding);
        if (lowercase) {
            clazz = clazz.toLowerCase();
        }

        return clazz;
    }

    private static String stripEnding(final String clazz) {
        if (!clazz.endsWith("ViewPresenter")) {
            return clazz;
        } else {
            final int viewIndex = clazz.lastIndexOf("ViewPresenter");
            return clazz.substring(0, viewIndex);
        }
    }

    @Override
    protected void onInitializationException(final Throwable throwable) {
        showError(throwable);
    }

    @Override
    protected void onInvocationException(final Throwable throwable) {
        showError(throwable);
    }

    @Override
    public Node getRootNode() {
        return pane;
    }

    protected Window getDialogOwner() {
        if (getRootNode() != null && getRootNode().getScene() != null && getRootNode().getScene().getWindow() != null) {
            return getRootNode().getScene().getWindow();
        }
        return null;
    }

    protected void showError(final Throwable throwable) {
        Platform.runLater(() -> {
            final UnexpectedErrorDialog unexpectedErrorDialog = new UnexpectedErrorDialog();
            unexpectedErrorDialog.setStackTrace(throwable);
            unexpectedErrorDialog.showAndWait();
        });
    }

    public Node getView() {
        return getRootNode();
    }

    @Override
    public SimpleBooleanProperty isWorkingProperty() {
        final SimpleBooleanProperty result = new SimpleBooleanProperty(true);
        modelProperty().addListener(observable -> {
            result.set(getModel().getIsWorking());
            getModel().isWorkingProperty().onChanged(evt -> result.setValue(evt.getNewValue()));
        });
        return result;
    }
}
