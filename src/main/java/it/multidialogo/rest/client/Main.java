package it.multidialogo.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.multidialogo.rest.client.Domain.Attachments;
import it.multidialogo.rest.client.Domain.File;
import it.multidialogo.rest.client.Domain.Recipient;
import it.multidialogo.rest.client.Domain.Sender;
import it.multidialogo.rest.client.Dto.PostQueueDto;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

import static it.multidialogo.rest.client.TokenWallet.storeTokens;

public class Main {

    private static CloseableHttpClient http;
    private static Gson gson;
    private static Scanner menuchoice;
    private static boolean done = false;

    public static void main(String[] args) throws IOException, ApiDialogException, URISyntaxException, NoSuchFieldException {
        inizialization();

        while (!done) {
            menu();

            int chosenScenario = getChosenScenario();

            if (chosenScenario == 0) {
                done = true;
                continue;
            }

            authenticate();

            switch (chosenScenario) {
                case 1:
                    scenario_1();
                    break;
                case 2:
                    scenario_2();
                    break;
                case 3:
                    scenario_3();
                    break;
                case 4:
                    scenario_4();
                    break;
                case 5:
                    scenario_5();
                    break;
                case 6:
                    scenario_6();
                    break;
                case 7:
                    scenario_7();
                    break;
                case 8:
                    scenario_8();
                    break;
                default:
                    System.out.println("Scenario non gestito: " + chosenScenario);
            }
        }
        finalization();
    }

    private static void menu() {
        System.out.println("");
        System.out.println("-------------------");
        System.out.println("Scenari disponibili");
        System.out.println("-------------------");
        System.out.println(" 1 - Invio a 3 destinatari: 3 con posta tradizionale ma affrancatura diversa");
        System.out.println(" 2 - Invio a 3 destinatari: 1 con posta tradizionale, 2 MultiCerta che generano due canali alternativi con affrancatura diversa");
        System.out.println(" 3 - Esempio con errore: file globale associato a un recipient");
        System.out.println(" 4 - Legge loghi impostati in preferenze");
        System.out.println(" 5 - Legge PEC impostata in preferenze");
        System.out.println(" 6 - Elenco utenti collegati all'utente principale");
        System.out.println(" 7 - Invio Certificazione Unica 2020");
        System.out.println(" 8 - Legge tipo di affrancatura impostata in preferenze");
        System.out.println(" 0 - Fine");
        System.out.print("Scegli lo scenario: ");
    }

    private static void inizialization() throws IOException {
        http = HttpClients.createDefault();
        gson = new GsonBuilder()
                .create();
        menuchoice = new Scanner(System.in);
        TokenWallet.readTokens();
    }

    public static int getChosenScenario() {
        return menuchoice.nextInt();
    }

    // Invio a 3 destinatari: 3 con posta tradizionale ma affrancatura diversa
    private static void scenario_1() throws IOException, ApiDialogException, URISyntaxException {
        String account = Utils.getAccount();
        String uploadSessionId = getUploadSessionId(account);
        if (uploadSessionId == null) {
            return;
        }
        Sender sender = Domain.createSender();

        File personale1 = postFile(account, uploadSessionId, "personale1.pdf", "private", null);
        File personale2 = postFile(account, uploadSessionId, "personale2.pdf", "private", null);
        File personale3 = postFile(account, uploadSessionId, "personale3.pdf", "private", null);
        File globale1 = postFile(account, uploadSessionId, "globale1.pdf", "global", Domain.createAttachmentOptions(true, "bw", "A4", 80, true, null));

        Attachments attachments = Domain.createAttachments(uploadSessionId, Arrays.asList(personale1, personale2, personale3, globale1));

        Recipient recipient1 = Domain.createRecipient("Via Emilia Ovest 129/2", "43126", "Parma", "PR", "it",
                "pt", "RACCOMANDATA1",
                "person", "Winton", "Marsalis", "Multidialogo Srl",
                "esempio1@catchall.netbuilder.it", null,
                Arrays.asList(personale1.getId()),
                "sendposta", null,
                null);

        Recipient recipient2 = Domain.createRecipient("Via Zarotto 63", "43123", "Collecchio", "PR", "it",
                "pt", "RACCOMANDATA1AR",
                "person", "Clara", "Schumann", "ASA Srl",
                "esempio2@catchall.netbuilder.it", null,
                Arrays.asList(personale2.getId()),
                "sendposta", null,
                null);

        Recipient recipient3 = Domain.createRecipient("Via Zarotto 63", "43123", "Collecchio", "PR", "it",
                "pt", "PRIORITARIA1",
                "person", "Amilcare", "Ponchielli", "AM Spa",
                "esempio3@catchall.netbuilder.it", "info@pec.testtest.it",
                Arrays.asList(personale3.getId()),
                "sendposta", null,
                null);

        PostQueueDto postQueueDto = Domain.createPostQueue(sender, attachments,
                Arrays.asList(recipient1, recipient2, recipient3),
                "Convocazione assemblea", "Caro sei convocato per l'assemblea. Visualizza l'allegato. Grazie.",
                false, false, false, false, "Test scenario 1", null);

        sendPostQueueRequest(account, postQueueDto);
    }

    // Invio a 3 destinatari: 1 con posta tradizionale, 2 MultiCerta che generano due canali alternativi con affrancatura diversa
    private static void scenario_2() throws IOException, ApiDialogException, URISyntaxException {
        String account = Utils.getAccount();
        String uploadSessionId = getUploadSessionId(account);
        if (uploadSessionId == null) {
            return;
        }
        Sender sender = Domain.createSender();

        File personale1 = postFile(account, uploadSessionId, "personale1.pdf", "private", null);
        File personale2 = postFile(account, uploadSessionId, "personale2.pdf", "private", null);
        File personale3 = postFile(account, uploadSessionId, "personale3.pdf", "private", null);
        File globale1 = postFile(account, uploadSessionId, "globale1.pdf", "global", Domain.createAttachmentOptions(true, "bw", "A4", 80, true, null));

        Attachments attachments = Domain.createAttachments(uploadSessionId, Arrays.asList(personale1, personale2, personale3, globale1));

        Recipient recipient1 = Domain.createRecipient("Via Emilia Ovest 129/2", "43126", "Parma", "PR", "it",
                "pt", "RACCOMANDATA1",
                "person", "Camille", "Saint Saëns", "Carnaval des animaux Srl",
                "esempio1@catchall.netbuilder.it", null,
                Arrays.asList(personale1.getId()),
                "sendposta", null,
                null);

        Recipient recipient2 = Domain.createRecipient("Via Zarotto 63", "43123", "Collecchio", "PR", "it",
                "pt", "RACCOMANDATA1AR",
                "person", "Maurice", "Ravel", "La Valse Srl",
                Constants.MULTICERTA_ENABLED_ADDRESS, null,
                Arrays.asList(personale2.getId()),
                "multicerta", "sendposta",
                null);

        Recipient recipient3 = Domain.createRecipient("Via Capelluti 11", "43043", "Borgotaro", "PR", "it",
                "pt", "PRIORITARIA1",
                "person", "Franz", "Schubert", "Die forelle Spa",
                Constants.MULTICERTA_ENABLED_ADDRESS, "info@pec.testtest.it",
                Arrays.asList(personale3.getId()),
                "multicerta", "sendposta",
                null);

        PostQueueDto postQueueDto = Domain.createPostQueue(sender, attachments,
                Arrays.asList(recipient1, recipient2, recipient3),
                "Convocazione assemblea", "Caro sei convocato per l'assemblea. Visualizza l'allegato. Grazie.",
                true, true, false, false, "Test scenario 5", null);

        sendPostQueueRequest(account, postQueueDto);
    }

    // Esempio di errore: file globale associato a un recipient
    private static void scenario_3() throws IOException, ApiDialogException, URISyntaxException {
        String account = Utils.getAccount();
        String uploadSessionId = getUploadSessionId(account);
        if (uploadSessionId == null) {
            return;
        }
        Sender sender = Domain.createSender();

        File personale1 = postFile(account, uploadSessionId, "personale1.pdf", "private", null);
        File personale2 = postFile(account, uploadSessionId, "personale2.pdf", "private", null);
        File globale1 = postFile(account, uploadSessionId, "globale1.pdf", "global", Domain.createAttachmentOptions(true, "bw", "A4", 80, true, null));

        Attachments attachments = Domain.createAttachments(uploadSessionId, Arrays.asList(personale1, personale2, globale1));

        Recipient recipient1 = Domain.createRecipient("Via Emilia Ovest 129/2", "43126", "Parma", "PR", "it",
                "pt", "RACCOMANDATA1",
                "person", "Winton", "Marsalis", "Multidialogo Srl",
                "esempio1@catchall.netbuilder.it", null,
                Arrays.asList(globale1.getId(), personale1.getId()),
                "sendposta", null,
                null);

        Recipient recipient2 = Domain.createRecipient("Via Zarotto 63", "43123", "Collecchio", "PR", "it",
                "pt", "RACCOMANDATA1AR",
                "person", "Clara", "Schumann", "ASA Srl",
                "esempio2@catchall.netbuilder.it", null,
                Arrays.asList(personale2.getId()),
                "sendposta", null,
                null);

        PostQueueDto postQueueDto = Domain.createPostQueue(sender, attachments,
                Arrays.asList(recipient1, recipient2),
                "Convocazione assemblea", "Caro sei convocato per l'assemblea. Visualizza l'allegato. Grazie.",
                false, false, false, false, "Test scenario 1", null);

        sendPostQueueRequest(account, postQueueDto);
    }

    // Legge loghi impostati in preferenze
    private static void scenario_4() throws IOException {
        String account = Utils.getAccount();
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/preferences";

        CloseableHttpResponse response = sendRequest(url, null, "Get");

        if (response == null) {
            return;
        }

        Preferences.UserPreferencesData userPreferencesData = gson.fromJson(getResponseAsString(response), Preferences.UserPreferencesData.class);
        System.out.println("Loghi:");
        System.out.println("------------------------");
        System.out.println("Id | Label | Default");
        System.out.println("------------------------");
        for (Preferences.SenderLetterWatermarkPreferences l : userPreferencesData.getLoghi())
            System.out.println(l.toString());
    }

    // Legge PEC impostata in preferenze
    private static void scenario_5() throws IOException {
        String account = Utils.getAccount();
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/preferences";

        CloseableHttpResponse response = sendRequest(url, null, "Get");

        if (response == null) {
            return;
        }

        Preferences.UserPreferencesData userPreferencesData = gson.fromJson(getResponseAsString(response), Preferences.UserPreferencesData.class);
        System.out.println("PEC:");
        System.out.println("-----------");
        System.out.println("Indirizzo");
        System.out.println("-----------");
        for (String pec : userPreferencesData.getPec()) System.out.println(pec);
    }

    // Elenco utenti collegati all'utente principale
    private static void scenario_6() throws IOException {
        String account = "me";
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/users?include=user-profiles";

        CloseableHttpResponse response = sendRequest(url, null, "Get");

        if (response == null) {
            return;
        }

        Dto.UserResponse userResponse = gson.fromJson(getResponseAsString(response), Dto.UserResponse.class);
        System.out.println("Utenti:");
        System.out.println("----------------------------------------------");
        System.out.println("Id | IsActive | Group | Username | Displayname");
        System.out.println("----------------------------------------------");
        for (Dto.UserExtended u : userResponse.getUsers())
            System.out.println(u);
    }

    // Invio Certificazione Unica 2020
    private static void scenario_7() throws IOException, URISyntaxException {
        String account = "me";
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/tax-withholding-transmission-sessions";
        String fileContent = Utils.createFileContent("esempio_cu.txt", "application/pdf");
        String json = gson.toJson(Dto.CuPostRequestDto.createCuPostRequestDto(fileContent));

        CloseableHttpResponse response = sendRequest(url, json, "Post");

        if (response == null) {
            return;
        }

        if (isSuccessfulStatusCode(response)) {
            System.out.println("CU inviata");
        } else {
            System.out.println("Si è verificato un errore! Dettagli:");
            handleErrors(getResponseAsString(response));
        }
    }

    // Legge tipo di affrancatura impostata in preferenze
    private static void scenario_8() throws IOException {
        String account = Utils.getAccount();
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/preferences";

        CloseableHttpResponse response = sendRequest(url, null, "Get");

        if (response == null) {
            return;
        }

        Preferences.UserPreferencesData userPreferencesData = gson.fromJson(getResponseAsString(response), Preferences.UserPreferencesData.class);
        System.out.println("Affrancatura impostata: " + userPreferencesData.getSenderPostageType());
    }

    private static File postFile(String account, String uploadSessionId, String fileName, String visibility, Domain.AttachmentOptions options) throws IOException, URISyntaxException, ApiDialogException {
        String fileContent = Utils.createFileContent(fileName, "application/pdf");
        String json = Utils.createPostFilePayload(fileName, fileContent, gson);
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/upload-sessions/" + uploadSessionId + "/uploaded-files";

        CloseableHttpResponse response = sendRequest(url, json, "Post");

        if (response == null) {
            throw new ApiDialogException("Impossibile ottenere fileId!");
        }

        Dto.GenericResponse dtoResponse = gson.fromJson(getResponseAsString(response), Dto.GenericResponse.class);
        String fileId = dtoResponse.getId();
        System.out.println("File id ricevuto: " + fileId);
        response.close();

        return new File(fileId, visibility, options);
    }

    private static void authenticate() throws IOException {
        if (TokenWallet.isCurrTokenEmpty()) {
            System.out.println("Richiesta di un nuovo token");
            login(Constants.REST_MULTIDIALOGO_STAGE_USERNAME, Constants.REST_MULTIDIALOGO_STAGE_PASSWORD);
        } else
            System.out.println("Riutilizzo token salvato " + TokenWallet.getCurrentTokens().getToken());
    }

    private static void login(String username, String password) throws IOException {
        String json = gson.toJson(Dto.createLoginRequestData(username, password));
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/login";

        CloseableHttpResponse response = http.execute(Utils.createRequest(Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/login", null, json, "Post"));

        String responseString = getResponseAsString(response);
        Dto.AuthResponse authResponse = gson.fromJson(responseString, Dto.AuthResponse.class);
        System.out.println("Nuovo token ricevuto");

        storeTokens(authResponse);
    }

    private static CloseableHttpResponse loginRefresh(String username) throws IOException {
        String token = TokenWallet.getRefreshToken();
        String json = gson.toJson(Dto.createRefreshTokenRequest(username, token));
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/login/refresh";

        CloseableHttpResponse response = http.execute(Utils.createRequest(url, token, json, "Post"));

        if (isSuccessfulStatusCode(response)) {
            String responseString = getResponseAsString(response);
            Dto.AuthResponse authResponse = gson.fromJson(responseString, Dto.AuthResponse.class);
            System.out.println("Refresh token ricevuto: " + authResponse.getTokenResponse().getRefreshToken());
            storeTokens(authResponse);
        }

        return response;
    }

    private static String getUploadSessionId(String account) throws IOException, ApiDialogException {
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/upload-sessions";

        CloseableHttpResponse response = sendRequest(url, Constants.EMPTY_JSON, "Post");

        if (response == null) {
            throw new ApiDialogException("Impossibile ottenere uploadSessionId");
        }

        Dto.GenericResponse uploadSessionResponse = gson.fromJson(getResponseAsString(response), Dto.GenericResponse.class);
        String uploadSessionId = uploadSessionResponse.getId();
        if (uploadSessionId == null) {
            System.out.println("Impossibile ottenere uploadSessionId!");
        } else {
            System.out.println("Upload session id ricevuto: " + uploadSessionId);
        }
        response.close();

        return uploadSessionId;
    }

    private static CloseableHttpResponse sendRequest(String url, String json, String method) throws IOException {
        CloseableHttpResponse response = null;
        boolean done = false;

        do {
            response = executeCall(url, json, method);

            if (isSuccessfulStatusCode(response)) {
                done = true;
            } else if (getStatusCode(response) == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                System.out.println("Server error (500)!");
                return null;
            } else if (getStatusCode(response) == HttpStatus.SC_NOT_FOUND) {
                System.out.println("NotFound error (404)!");
                return null;
            } else if (getStatusCode(response) == HttpStatus.SC_FORBIDDEN) {
                System.out.println("Forbidden error (403)!");
                return null;
            } else if (getStatusCode(response) == HttpStatus.SC_BAD_REQUEST) {
                System.out.println("Bad request error (400)!");
                done = true;
            } else if (getStatusCode(response) == HttpStatus.SC_UNAUTHORIZED) {
                response.close();
                System.out.println("Token scaduto -> Refresh");
                response = loginRefresh(Constants.REST_MULTIDIALOGO_STAGE_USERNAME);
                if (getStatusCode(response) == HttpStatus.SC_UNAUTHORIZED) {
                    System.out.println("Refresh token scaduto -> Login");
                    login(Constants.REST_MULTIDIALOGO_STAGE_USERNAME, Constants.REST_MULTIDIALOGO_STAGE_PASSWORD);
                }
            }
        } while (!done);

        return response;
    }

    private static String getResponseAsString(CloseableHttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    private static boolean isSuccessfulStatusCode(CloseableHttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return status == HttpStatus.SC_CREATED || status == HttpStatus.SC_OK;
    }

    private static CloseableHttpResponse executeCall(String url, String json, String method) throws IOException {
        HttpRequestBase request = Utils.createCurrTokenRequest(url, json, method);
        return http.execute(request);
    }

    private static int getStatusCode(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private static void sendPostQueueRequest(String account, PostQueueDto postQueueDto) throws ApiDialogException, IOException {
        String url = Constants.REST_MULTIDIALOGO_STAGE_HOST + "/users/" + account + "/queues";
        String json = gson.toJson(postQueueDto);

        CloseableHttpResponse response = sendRequest(url, json, "Post");

        if (response == null) {
            throw new ApiDialogException("Impossibile creare la coda");
        }

        if (getStatusCode(response) == HttpStatus.SC_CREATED) {
            System.out.println("Coda creata");
        } else {
            System.out.println("Si è verificato un errore! Dettagli: ");
            String responseAsString = getResponseAsString(response);
            System.out.println(responseAsString);
            handleErrors(responseAsString);
        }

        response.close();
    }

    private static void handleErrors(String responseString) {
        Dto.ErrorResponse errorResponse = gson.fromJson(responseString, Dto.ErrorResponse.class);

        for (Dto.Parameter p : errorResponse.getParameters()) {
            ErrorUtils.parseDotNotation(p.parameter);
            System.out.println(p.parameter);
            p.getMessages().forEach(m -> System.out.println("\t" + m));
        }
    }

    private static void finalization() throws IOException {
        http.close();
    }

}
