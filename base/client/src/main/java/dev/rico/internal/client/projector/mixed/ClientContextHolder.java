package dev.rico.internal.client.projector.mixed;


import dev.rico.client.remoting.ClientContext;

public class ClientContextHolder {

	private static ClientContext context;

	private ClientContextHolder() {}

	public static synchronized ClientContext getContext() {
		return context;
	}

    public static synchronized void setContext(final ClientContext context) {
        ClientContextHolder.context = context;
    }
}
