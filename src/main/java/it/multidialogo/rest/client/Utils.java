package it.multidialogo.rest.client;

import com.google.gson.Gson;
import it.multidialogo.rest.client.Dto.UploadFiledRequestData;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

public class Utils {

    private static StringEntity getPostEntity(String jsonParam) {
        StringEntity ret = new StringEntity(jsonParam, "UTF-8");
        ret.setContentType("application/json");
        return ret;
    }

    private static String getEncodedFileContent(Path path) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(path));
    }

    private static Path getPath(String fileName) throws URISyntaxException {
        URI uri = Main.class.getClassLoader().getResource(fileName).toURI();
        return Paths.get(uri);
    }

    private static void addHeadersToRequest(String token, String json, HttpRequestBase request) {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        if (token != null) {
            request.setHeader("Authorization", "Bearer " + token);
        }
        if (json != null) {
            ((HttpPost) request).setEntity(getPostEntity(json));
        }
        request.setHeader("X-api-client-name", Constants.X_API_CLIENT_NAME);
        request.setHeader("X-api-client-version", Constants.X_API_CLIENT_VERSION);
        //
        // ATTENZIONE:
        // l'api key dovrebbe essere custodita in maniera sicura, non salvata in chiaro su disco, ad esempio.
        //
        request.setHeader("X-api-key", Constants.X_API_KEY);
    }

    public static String createFileContent(String fileName, String mimeType) throws URISyntaxException, IOException {
        String encodedFileContent = getEncodedFileContent(getPath("files/" + fileName));
        return "data:" + mimeType + ";base64," + encodedFileContent;
    }

    public static String createPostFilePayload(String fileName, String fileContent, Gson gson) {
        UploadFiledRequestData uploadFiledRequestData = Dto.createUploadFileRequestData(fileName, fileContent);
        return gson.toJson(uploadFiledRequestData);
    }

    public static HttpRequestBase createCurrTokenRequest(String url, String json, String method) {
        return createRequest(url, TokenWallet.getCurrentTokens().getToken(), json, method);
    }

    public static HttpRequestBase createRequest(String url, String token, String json, String method) {
        return method.equals("Post") ? getHttpPostRequest(url, token, json) : getHttpGetRequest(url, token);
    }

    private static HttpPost getHttpPostRequest(String path, String token, String json) {
        HttpPost request = new HttpPost(path);
        addHeadersToRequest(token, json, request);
        return request;
    }

    private static HttpRequestBase getHttpGetRequest(String path, String token) {
        HttpRequestBase request = new HttpGet(path);
        addHeadersToRequest(token, null, request);
        return request;
    }

    public static String getAccount() {
        System.out.println("Inserire l'account desiderato: \\\"me\\\" per l'amministratore, l'id per uno dei sottoutenti (condomini)");
        System.out.println("NB: la API che ritorna gli id dei sottoutenti disponibili è mostrata nell'esempio 6 - utenti collegati");
        return new Scanner(System.in).next();
    }
}
