package dev.rico.internal.client.projector.mixed;

//TODO: Class is not used, can we delete it?
public class Configuration {
    public static String getServerUrl() {
        return System.getProperty("server", "http://localhost:8888");
    }

    public static String getSecurityEndpointUrl() {
        return getServerUrl() + "/openid-connect";
    }
}
