package it.multidialogo.rest.client;

import java.util.ArrayList;
import java.util.List;

public class Domain {

    public static Attachments createAttachments(String uploadSessionId, List<File> fileList) {
        return new Attachments(uploadSessionId, fileList);
    }

    public static class Email {
        private final String displayAddress;
        private final String notificationAddress;
        private final String certifiedAddress;

        public Email(String displayAddress, String notificationAddress, String certifiedAddress) {
            this.displayAddress = displayAddress;
            this.notificationAddress = notificationAddress;
            this.certifiedAddress = certifiedAddress;
        }
    }

    public static class Sender {
        private final String companyName;
        private final String streetAddress;
        private final String admLvl3;
        private final String admLvl2;
        private final String country;
        private final String zipCode;
        private final String vatCode;
        private final Email email;

        public Sender(String companyName, String streetAddress, String admLvl3, String admLvl2, String country, String zipCode, String vatCode, Email email) {
            this.companyName = companyName;
            this.streetAddress = streetAddress;
            this.admLvl3 = admLvl3;
            this.admLvl2 = admLvl2;
            this.country = country;
            this.zipCode = zipCode;
            this.vatCode = vatCode;
            this.email = email;
        }
    }

    public static Sender createSender() {
        Email email = new Email(Constants.SENDER_DISPLAY_ADDRESS, Constants.SENDER_NOTIFICATION_ADDRESS, Constants.SENDER_CERTIFIED_ADDRESS);
        return new Sender(Constants.SENDER_COMPANY_NAME,
                Constants.SENDER_STREET_ADDRESS,
                Constants.SENDER_ADM_LVL3,
                Constants.SENDER_ADM_LVL2,
                Constants.SENDER_COUNTRY,
                Constants.SENDER_ZIP_CODE,
                Constants.SENDER_VAT_CODE,
                email
        );
    }

    public static class PrintOptions {
        private final boolean frontBack;
        private final String colorMode;
        private final String sheetFormat;
        private final Integer weight;
        private final boolean staple;
        private final Boolean globalStaple;

        public PrintOptions(boolean frontBack, String colorMode, String sheetFormat, Integer weight, boolean staple, Boolean globalStaple) {
            this.frontBack = frontBack;
            this.colorMode = colorMode;
            this.sheetFormat = sheetFormat;
            this.weight = weight;
            this.staple = staple;
            this.globalStaple = globalStaple;
        }
    }

    public static class AttachmentOptions {
        private final PrintOptions print;

        public AttachmentOptions(PrintOptions print) {
            this.print = print;
        }
    }

    public static AttachmentOptions createAttachmentOptions(boolean frontBack, String colorMode, String sheetFormat, Integer weight, boolean staple, Boolean globalStaple) {
        return new AttachmentOptions(new PrintOptions(frontBack, colorMode, sheetFormat, weight, staple, globalStaple));
    }

    public static class File {
        private final String id;
        private final String visibility;
        private final AttachmentOptions options;

        public String getId() {
            return id;
        }

        public File(String id, String visibility, AttachmentOptions options) {
            this.id = id;
            this.visibility = visibility;
            this.options = options;
        }
    }

    public static class Attachments {
        private final String uploadSessionId;
        private final List<File> files;

        public Attachments(String uploadSessionId, List<File> files) {
            this.uploadSessionId = uploadSessionId;
            this.files = files;
        }
    }

    public static class PostalInfoName {
        private final String type;
        private final String firstname;
        private final String lastname;
        private final String companyName;

        public PostalInfoName(String type, String firstname, String lastname, String companyName) {
            this.type = type;
            this.firstname = firstname;
            this.lastname = lastname;
            this.companyName = companyName;
        }
    }

    public static class Postage {
        private final String vector;
        private final String type;

        public Postage(String vector, String type) {
            this.vector = vector;
            this.type = type;
        }
    }

    public static class PostalInfo {
        private final String streetAddress;
        private final String zipCode;
        private final String admLvl3;
        private final String admLvl2;
        private final String countryCode;
        private final Postage postage;
        private final PostalInfoName name;

        public PostalInfo(String streetAddress, String zipCode, String admLvl3, String admLvl2, String countryCode, Postage postage, PostalInfoName name) {
            this.streetAddress = streetAddress;
            this.zipCode = zipCode;
            this.admLvl3 = admLvl3;
            this.admLvl2 = admLvl2;
            this.countryCode = countryCode;
            this.postage = postage;
            this.name = name;
        }
    }

    public static PostalInfo createPostalInfo(String streetAddress, String zipCode, String admLvl3, String admLvl2, String countryCode, String postageVector, String postageType, String type, String firstname, String lastname, String companyName) {
        Postage postage = postageVector != null ? new Postage(postageVector, postageType) : null;
        return new PostalInfo(streetAddress, zipCode, admLvl3, admLvl2, countryCode, postage, new PostalInfoName(type, firstname, lastname, companyName));
    }

    public static class RecipientAttachment {
        private final List<String> files;

        public RecipientAttachment(List<String> files) {
            this.files = files;
        }
    }

    public static class Carrier {
        private final String channel;
        private final String alternativeChannel;

        public Carrier(String channel, String alternativeChannel) {
            this.channel = channel;
            this.alternativeChannel = alternativeChannel;
        }
    }

    public static class Keyword {
        private final String placeholder;
        private final String value;

        public Keyword(String placeholder, String value) {
            this.placeholder = placeholder;
            this.value = value;
        }
    }

    public static class MessageOptions {
        private final List<Keyword> keywords;

        public MessageOptions(List<Keyword> keywords) {
            this.keywords = keywords;
        }
    }

    public static class Recipient {
        private final String email;
        private final String pec;
        private final PostalInfo postalInfo;
        private final RecipientAttachment attachments;
        private final Carrier carrier;
        private final MessageOptions messageOptions;
        private final List<CustomDataElement> customData;

        public Recipient(String email, String pec, PostalInfo postalInfo, RecipientAttachment attachments, Carrier carrier, MessageOptions messageOptions, List<CustomDataElement> customData) {
            this.email = email;
            this.pec = pec;
            this.postalInfo = postalInfo;
            this.attachments = attachments;
            this.carrier = carrier;
            this.messageOptions = messageOptions;
            this.customData = customData;
        }
    }

    private static Recipient createRecipient(String email, String pec, PostalInfo postalInfo, List<String> attachmentFiles, String channel, String alternativeChannel, List<Keyword> keywords, List<CustomDataElement> customData) {
        MessageOptions messageOptions = keywords != null ? new MessageOptions(keywords) : null;
        return new Recipient(email, pec, postalInfo, new RecipientAttachment(attachmentFiles), new Carrier(channel, alternativeChannel), messageOptions, customData);
    }

    public static Recipient createRecipient(
            String streetAddress, String zipCode, String admLvl3, String admLvl2, String countryCode, String postageVector, String postageType, String type, String firstname, String lastname, String companyName,
            String email, String pec, List<String> attachmentFiles, String channel, String alternativeChannel, List<Keyword> keywords) {
        PostalInfo postalInfo = createPostalInfo(streetAddress, zipCode, admLvl3, admLvl2, countryCode, postageVector, postageType, type, firstname, lastname, companyName);
        return createRecipient(email, pec, postalInfo, attachmentFiles, channel, alternativeChannel, keywords, null);
    }

    public static Recipient createRecipient(
            String streetAddress, String zipCode, String admLvl3, String admLvl2, String countryCode, String postageVector, String postageType, String type, String firstname, String lastname, String companyName,
            String email, String pec, List<String> attachmentFiles, String channel, String alternativeChannel, List<Keyword> keywords, List<CustomDataElement> customData) {
        PostalInfo postalInfo = createPostalInfo(streetAddress, zipCode, admLvl3, admLvl2, countryCode, postageVector, postageType, type, firstname, lastname, companyName);
        return createRecipient(email, pec, postalInfo, attachmentFiles, channel, alternativeChannel, keywords, customData);
    }

    public static class Message {
        private final String subject;
        private final String body;

        public Message(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }
    }

    public static class Billing {
        private final String invoiceTag;

        public Billing(String invoiceTag) {
            this.invoiceTag = invoiceTag;
        }
    }

    public static class Multicerta {
        private final boolean legalValue;

        public Multicerta(boolean legalValue) {
            this.legalValue = legalValue;
        }
    }

    public static class Deadline {
        private final String acknowledgement;
        private final int duration;

        public Deadline(String acknowledgement, int duration) {
            this.acknowledgement = acknowledgement;
            this.duration = duration;
        }
    }


    public static class Postal {
        private final boolean expedite;
        private final PrintOptions print;

        public Postal(boolean expedite, PrintOptions print) {
            this.expedite = expedite;
            this.print = print;
        }
    }

    public static class Options {
        private final Billing billing;
        private final Multicerta multicerta;
        private final Deadline deadline;
        private final Postal postal;

        public Options(Billing billing, Multicerta multicerta, Deadline deadline, Postal postal) {
            this.billing = billing;
            this.multicerta = multicerta;
            this.deadline = deadline;
            this.postal = postal;
        }
    }

    public static class CustomDataElement {
        private final String key;
        private final String value;
        private final String visibility;

        public CustomDataElement(String key, String value, String visibility) {
            this.key = key;
            this.value = value;
            this.visibility = visibility;
        }
    }

    public static List<CustomDataElement> createCustomData(String key, String value, String visibility) {
        List<CustomDataElement> ret = new ArrayList<>();
        ret.add(new CustomDataElement(key, value, visibility));
        return ret;
    }

    public static class PostQueue {
        private final String type;
        private final Sender sender;
        private final Attachments attachments;
        private final List<Recipient> recipients;
        private final Message message;
        private final Options options;
        private final String topic;
        private final List<CustomDataElement> customData;

        public PostQueue(String type, Sender sender, Attachments attachments, List<Recipient> recipients, Message message, Options options, String topic, List<CustomDataElement> customData) {
            this.type = type;
            this.sender = sender;
            this.attachments = attachments;
            this.recipients = recipients;
            this.message = message;
            this.options = options;
            this.topic = topic;
            this.customData = customData;
        }
    }

    public static Dto.PostQueueDto createPostQueue(Domain.Sender sender, Domain.Attachments attachments, List<Domain.Recipient> recipients, String subject, String body, boolean useMulticerta, boolean useMulticertaLegal, boolean staple, boolean globalStaple, String topic, List<Domain.CustomDataElement> customData) {
        Domain.Message message = new Domain.Message(subject, body);
        Domain.Billing billing = new Domain.Billing("INVOICE_" + topic.replace(" ", "_").toUpperCase());
        Domain.Multicerta multicerta = null;
        Domain.Deadline deadline = null;
        if (useMulticerta) {
            multicerta = new Domain.Multicerta(useMulticertaLegal);
            deadline = new Domain.Deadline("read", 3600);
        }
        Domain.Postal postal = new Domain.Postal(true, new Domain.PrintOptions(true, "color", null, null, staple, globalStaple));
        Domain.Options options = new Domain.Options(billing, multicerta, deadline, postal);
        return new Dto.PostQueueDto(new Dto.DataPayload<>(new Domain.PostQueue("concrete", sender, attachments, recipients, message, options, topic, customData)));
    }
}
