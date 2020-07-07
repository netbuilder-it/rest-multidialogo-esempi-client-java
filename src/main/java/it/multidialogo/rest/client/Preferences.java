package it.multidialogo.rest.client;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Preferences {

    public static class AppPush {
        public boolean blocked;
        public boolean delivered;
        public boolean refused;
    }

    public static class Credit {
        public float serviceThreshold;
        public float postageThreshold;
        public String notificationEmail;
    }

    public static class DailyDigest {
        public List<String> queue;
    }

    public static class Space {
        public int threshold;
        public String notificationEmail;
    }

    public static class NotificationPreferences {
        public AppPush appPush;
        public Credit credit;
        public DailyDigest dailyDigest;
        public Space space;
    }

    public static class SenderMulticertaPostageUserPreferences {
        public String legalPostageTypeLabel;
        public String postageTypeLabel;
    }

    public static class SenderMulticertaDeadlineUserPreferences {
        public String acknowledgement;
        public int duration;
        public String legalAcknowledgement;
        public int legalDuration;
    }

    public static class SenderMulticertaPreferences {
        public SenderMulticertaPostageUserPreferences postage;
        public SenderMulticertaDeadlineUserPreferences deadline;
    }

    public static class SenderEmailPreferences {
        public List<String> certifiedAddresses;
        public String notificationAddress;
        public String displayAddress;
    }

    public static class SenderFaxPreferences {
        public String name;
        public String prefix;
        public String number;
        public String notificationEmail;
        public String cover;
    }

    public static class SenderSmsPreference {
        public String type;
        public String display;
        public String prefix;
        public String number;
        public String alias;
        public String notificationEmail;
    }

    public static class SenderLetterWatermarkPreferences {
        public int id;
        public String label;
        @SerializedName("default")
        public boolean _default;

        @Override
        public String toString() {
            return String.format("%d %s %s", id, label, _default);
        }
    }

    public static class SenderLetterPrintPreferences {
        public boolean frontBack;
        public String colorMode;
        public String sheetFormat;
        public int weight;
        public boolean staple;
    }

    public static class SenderLetterPostageVectorsPreferences {
        public String type;
        public String vector;
    }

    public static class SenderLetterPostageVectorEnabling {
        public String vector;
        public String enabledAt;
        public String disabledAt;
    }

    public static class SenderLetterPostagePreferences {
        public String type;
        public List<SenderLetterPostageVectorsPreferences> vectors;
        public List<SenderLetterPostageVectorEnabling> vectorEnablings;
        public boolean topicOnReturnReceipt;
    }

    public static class SenderLetterPreferences {
        public List<SenderLetterWatermarkPreferences> watermarks;
        public SenderLetterPostagePreferences postage;
        public SenderLetterPrintPreferences print;
        public String zipCode;
        public String streetAddress;
        public String admLvl2;
        public String admLvl3;
        public String countryCodeOrStateName;
        public String notificationEmail;

        public String getPostageType() {
            return postage != null ? postage.type : null;
        }
    }

    public static class SenderPreferences {
        public SenderMulticertaPreferences multicerta;
        public SenderEmailPreferences email;
        public SenderFaxPreferences fax;
        public SenderSmsPreference sms;
        public SenderLetterPreferences letter;

        public List<SenderLetterWatermarkPreferences> getLoghi() {
            return letter == null || letter.watermarks == null ? new ArrayList<>() : letter.watermarks;
        }

        public List<String> getPec() {
            return email != null ? email.certifiedAddresses : new ArrayList<>();
        }

        public String getPostageType() {
            return letter != null ? letter.getPostageType() : null;
        }

    }

    public static class MultiboxRecipientEnvelopeRecipientListPreferences {
        public String subject;
    }

    public static class MultiboxRecipientEnvelopePreferences {
        @SerializedName("package")
        public String _package;
        public MultiboxRecipientEnvelopeRecipientListPreferences recipientList;
    }

    public static class MultiboxRecipientPreferences {
        public String firstname;
        public String lastname;
        public String companyName;
        public String addressee;
        public String streetAddress;
        public String zipCode;
        public String admLvl2;
        public String admLvl3;
        public MultiboxRecipientEnvelopePreferences envelop;
    }

    public static class RecipientPreferences {
        public MultiboxRecipientPreferences multibox;
    }

    public static class UserPreference {
        public NotificationPreferences notification;
        public SenderPreferences sender;
        public RecipientPreferences recipient;

        public List<SenderLetterWatermarkPreferences> getLoghi() {
            return sender != null ? sender.getLoghi() : new ArrayList<>();
        }

        public List<String> getPec() {
            return sender != null ? sender.getPec() : new ArrayList<>();
        }

        public String getSenderPostageType() {
            return sender != null ? sender.getPostageType() : null;
        }

    }

    public static class UserPreferenceSet {
        public int userId;
        public UserPreference preset;
        public UserPreference current;
        public UserPreference requested;
    }

    public static class UserPreferencesData {
        public Dto.DataPayload<UserPreferenceSet> data;

        public List<SenderLetterWatermarkPreferences> getLoghi() {
            List<SenderLetterWatermarkPreferences> ret = new ArrayList<>();
            if (data.getAttributes().requested != null) {
                ret.addAll(data.getAttributes().requested.getLoghi());
            }
            if (data.getAttributes().current != null) {
                ret.addAll(data.getAttributes().current.getLoghi());
            }
            if (data.getAttributes().preset != null) {
                ret.addAll(data.getAttributes().preset.getLoghi());
            }
            return ret;
        }

        public List<String> getPec() {
            List<String> ret = new ArrayList<>();
            if (data.getAttributes().requested != null) {
                ret.addAll(data.getAttributes().requested.getPec());
            }
            if (data.getAttributes().current != null) {
                ret.addAll(data.getAttributes().current.getPec());
            }
            if (data.getAttributes().preset != null) {
                ret.addAll(data.getAttributes().preset.getPec());
            }
            return ret;
        }

        public String getSenderPostageType() {
            UserPreferenceSet attributes = data.getAttributes();
            if (attributes.requested != null && attributes.requested.getSenderPostageType() != null) {
                return attributes.requested.getSenderPostageType();
            } else if (attributes.current != null && attributes.current.getSenderPostageType() != null) {
                return attributes.current.getSenderPostageType();
            } else if (attributes.preset != null && attributes.preset.getSenderPostageType() != null) {
                return attributes.preset.getSenderPostageType();
            }
            return null;
        }
    }

}
