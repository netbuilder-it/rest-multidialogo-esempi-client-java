# API Rest Multidialogo

## Esempi d'uso
Questa è un'applicazione di tipo console scritta in Java 8 che mostra alcuni esempi d'uso significativi delle API di Rest Multidialogo.

## Requisiti
- JDK 8
- Maven 3.x

### Per compilare il progetto 
````
mvn clean package
````

### Per eseguire
````
mvn exec:java -Dexec.mainClass="Main"
````

## Impostazioni iniziali 
Nel file Constants.java è necessario impostare una serie di parametri di identificazione, che sono spiegati sotto.

Le credenziali fornite per l'accesso alla piattaforma di stage beta.multidialogo.it:

```
    public static final String REST_MULTIDIALOGO_STAGE_USERNAME = "inserire_username";
    public static final String REST_MULTIDIALOGO_STAGE_PASSWORD = "inserire_password";
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
    public static final String X_API_CLIENT_VERSION = "";
```

Le credenziali di accesso e di identificazione del client verranno fornite separatamente.


