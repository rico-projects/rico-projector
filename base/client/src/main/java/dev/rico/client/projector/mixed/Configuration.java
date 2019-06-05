package dev.rico.client.projector.mixed;

public class Configuration {
    public static String getServerUrl() {
        return System.getProperty("server", "http://localhost:8888");
    }

    public static String getSecurityEndpointUrl() {
        return getServerUrl() + "/openid-connect";
    }
}
