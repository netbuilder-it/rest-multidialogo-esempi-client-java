# API Rest Multidialogo

## Esempi d'uso

### Requisiti
Questa è un'applicazione di tipo console scritta in Java 8 e con Maven per la gestione delle dipendenze.  

## Impostazioni iniziali 
Nella classe ```Constants``` è necessario impostare una serie di parametri di identificazione, che sono spiegati sotto.

Le credenziali fornite per l'accesso alla piattaforma di stage beta.multidialogo.it:

```
    public static final String REST_MULTIDIALOGO_STAGE_USERNAME  = "";
    public static final String REST_MULTIDIALOGO_STAGE_PASSWORD = "";
```
I dati del mittente:
```
    public static final String SENDER_DISPLAY_ADDRESS = "";
    public static final String SENDER_NOTIFICATION_ADDRESS = "";
    public static final String SENDER_CERTIFIED_ADDRESS = "";
    public static final String SENDER_COMPANY_NAME = "";
    public static final String SENDER_STREET_ADDRESS = "";
    public static final String SENDER_ADM_LVL3 = "";
    public static final String SENDER_ADM_LVL2 = "";
    public static final String SENDER_COUNTRY = "";
    public static final String SENDER_ZIP_CODE = "";
    public static final String SENDER_VAT_CODE = "";
```
L'indirizzo del destinatario che ha la Multicerta attiva (necessario per l'esempio d'uso della Multicerta):
```
    public static final String MULTICERTA_ENABLED_ADDRESS = "";
```
I parametri per l'autenticazione del client:
```
    public static final String X_API_CLIENT_NAME = "";
    public static final String X_API_KEY = "";
```

Le credenziali di accesso e di identificazione del client verranno fornite separatamente.

## Build e esecuzione
L'applicazione si può compilare con il seguente comando Maven da command shell:
```
mvn clean package
```
Per eseguirla:
```
mvn exec:java -Dexec.mainClass=it.multidialogo.rest.client.Main
```
