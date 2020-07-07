package it.multidialogo.rest.client;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class TokenWallet {
    //
    // ATTENZIONE:
    // i token dovrebbero essere custoditi in maniera sicura. Ad esempio _non_ salvati in chiaro su disco.
    //
    private static Dto.TokenResponse tokens;

    public static void readTokens() throws IOException {
        String appConfigPath = getPropertiesPath();
        Properties appProps = getProperties(appConfigPath);
        String token = appProps.getProperty("token");
        String refreshToken = appProps.getProperty("refreshToken");
        tokens = new Dto.TokenResponse(token, refreshToken);
    }

    private static Properties getProperties(String appConfigPath) throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        return appProps;
    }

    private static String getPropertiesPath() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return rootPath + "app.properties";
    }

    public static Dto.TokenResponse getCurrentTokens() {
        return tokens;
    }

    public static boolean isCurrTokenEmpty() {
        return tokens == null || tokens.getToken() == null || tokens.getToken().isEmpty();
    }

    public static String getRefreshToken() {
        return tokens.getRefreshToken();
    }

    public static void storeTokens(Dto.AuthResponse authResponse) throws IOException {
        tokens = authResponse.getTokenResponse();
        writeTokens();
    }

    private static void writeTokens() throws IOException {
        Properties appProps = new Properties();
        appProps.setProperty("token", tokens.getToken());
        appProps.setProperty("refreshToken", tokens.getRefreshToken());
        appProps.store(new FileWriter(getPropertiesPath()), null);
    }

}
