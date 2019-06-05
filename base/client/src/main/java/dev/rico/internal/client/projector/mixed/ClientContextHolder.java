package dev.rico.internal.client.projector.mixed;


import dev.rico.client.remoting.ClientContext;

public class ClientContextHolder {
	private static ClientContext context;

	public synchronized static ClientContext getContext() {
		return context;
	}

    public synchronized static void setContext(ClientContext context) {
        ClientContextHolder.context = context;
    }
}
