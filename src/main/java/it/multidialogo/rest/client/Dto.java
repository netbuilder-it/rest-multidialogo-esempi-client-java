package it.multidialogo.rest.client;

import java.util.ArrayList;
import java.util.List;

public class Dto {

    public static class AuthResponse {
        public String status;
        public DataPayload<TokenResponse> data;

        public TokenResponse getTokenResponse() {
            if (data == null || data.getAttributes() == null) {
                return null;
            } else {
                return data.getAttributes();
            }
        }
    }

    public static class DataPayload<T> {
        private String id;
        private String type;
        private final T attributes;

        public String getId() {
            return id;
        }

        public T getAttributes() {
            return attributes;
        }

        public DataPayload(T attributes) {
            this.attributes = attributes;
        }
    }

    public static class GenericResponse {
        private String status;
        private DataPayload<Object> data;

        public String getStatus() {
            return status;
        }

        public String getId() {
            return data != null ? data.getId() : null;
        }
    }

    public static class Parameter {
        String parameter;
        String code;
        List<String> messages;

        public List<String> getMessages() {
            return messages;
        }
    }

    public static class Source {
        List<Parameter> parameters;

        public List<Parameter> getParameters() {
            return parameters;
        }
    }

    public static class ErrorResponse {
        String id;
        String status;
        String code;
        String title;
        String detail;
        Source source;

        public List<Parameter> getParameters() {
            return source.getParameters();
        }
    }

    public static class LoginRequest {
        private final String username;
        private final String password;
        private final String grantType;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
            this.grantType = "password";
        }
    }

    public static class LoginRequestData {
        public DataPayload<LoginRequest> data;

        public LoginRequestData(DataPayload<LoginRequest> data) {
            this.data = data;
        }

    }

    public static LoginRequestData createLoginRequestData(String username, String password) {
        return new LoginRequestData(new DataPayload<>(new LoginRequest(username, password)));
    }

    public static class TokenResponse {
        private String token;
        private String refreshToken;
        private String category;

        public String getToken() {
            return token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public TokenResponse(String token, String refreshToken) {
            this.token = token;
            this.refreshToken = refreshToken;
        }
    }

    public static class UploadFiledRequestData {
        public DataPayload<UploadFileRequest> data;

        public UploadFiledRequestData(DataPayload<UploadFileRequest> data) {
            this.data = data;
        }
    }

    public static UploadFiledRequestData createUploadFileRequestData(String fileName, String fileContent) {
        return new UploadFiledRequestData(new DataPayload<>(new UploadFileRequest(fileName, fileContent)));
    }

    public static class UploadFileRequest {
        public String filename;
        public String fileData;
        public String customId;

        public UploadFileRequest(String filename, String fileData) {
            this.filename = filename;
            this.fileData = fileData;
        }
    }

    public static class RefreshToken {
        private final String username;
        private final String refreshToken;
        private final String grantType;

        public RefreshToken(String username, String refreshToken) {
            this.username = username;
            this.refreshToken = refreshToken;
            this.grantType = "refresh-token";
        }
    }

    public static class RefreshTokenRequest {
        private final DataPayload<RefreshToken> data;

        public RefreshTokenRequest(DataPayload<RefreshToken> data) {
            this.data = data;
        }
    }

    public static RefreshTokenRequest createRefreshTokenRequest(String username, String refreshToken) {
        return new RefreshTokenRequest(new DataPayload<>(new RefreshToken(username, refreshToken)));
    }

    public static class PostQueueDto {
        private final DataPayload<Domain.PostQueue> data;

        public PostQueueDto(DataPayload<Domain.PostQueue> data) {
            this.data = data;
        }
    }

    public static class User {
        public boolean isActive;
        public String group;

        @Override
        public String toString() {
            return isActive + " " + group;
        }
    }

    public static class Profile {
        public String id;
        public String type;

        @Override
        public String toString() {
            return id + " " + type;
        }
    }

    public static class ProfileData {
        public Profile data;
    }

    public static class ProfileEnv {
        public ProfileData profile;
    }

    public static class UserDto {
        public String id;
        public String type;
        public User attributes;
        public ProfileEnv relationships;

        @Override
        public String toString() {
            return id + " " + type + " " + attributes + " " + relationships;
        }
    }

    public static class Meta {
        public int total;
    }

    public static class UserProfile {
        public String username;
        public String displayName;

        @Override
        public String toString() {
            return username + " " + displayName;
        }
    }

    public static class UserExtended {
        public String id;
        public User user;
        public UserProfile profile;

        @Override
        public String toString() {
            return id + " " + user + " " + profile;
        }
    }

    public static class UserResponse {
        public String status;
        public Meta meta;
        public List<UserDto> data;
        public List<DataPayload<UserProfile>> included;

        public List<UserExtended> getUsers() {
            List<UserExtended> ret = new ArrayList<>();
            List<UserDto> users = data != null ? data : new ArrayList<>();
            for (UserDto u : users) {
                UserExtended ue = new UserExtended();
                ue.id = u.id;
                ue.user = u.attributes;
                ue.profile = findProfile(u.id);
                ret.add(ue);
            }
            return ret;
        }

        private UserProfile findProfile(String userId) {
            if (included == null) {
                return null;
            }
            DataPayload<UserProfile> payload = included.stream().filter(up -> up.id.equals(userId)).findAny().orElse(null);
            return payload != null ? payload.getAttributes() : null;
        }
    }

    public static class CuTrack {
        private final String fileData;

        public CuTrack(String fileData) {
            this.fileData = fileData;
        }
    }

    public static class CuPostRequest {
        public final String label;
        public final String countryCode;
        public final String billingMode;
        public final CuTrack track;
        public final String type;

        public CuPostRequest(String fileData) {
            this.label = "Esempio";
            this.countryCode = "it";
            this.billingMode = "CLAIM";
            this.type = "CU";
            this.track = new CuTrack(fileData);
        }
    }

    public static class CuPostRequestDto {
        public final DataPayload<CuPostRequest> data;

        public CuPostRequestDto(CuPostRequest cuPostRequest) {
            this.data = new DataPayload<>(cuPostRequest);
        }

        public static CuPostRequestDto createCuPostRequestDto(String fileData) {
            return new CuPostRequestDto((new CuPostRequest(fileData)));
        }
    }

    public static class PostSmsQueueRequest {
        public final Domain.SmsSender sender;
        public final String type = "sms-queue";
        public final String topic;
        public final String message;
        private final Domain.SmsQueueOptions options;
        public final List<Domain.CustomDataElement> customData = new ArrayList<>();
        public final List<Domain.SmsRecipient> recipients = new ArrayList<>();

        public PostSmsQueueRequest(Domain.SmsSender sender,
                                   String topic,
                                   String message,
                                   Domain.SmsQueueOptions options) {
            this.sender = sender;
            this.topic = topic;
            this.message = message;
            this.options = options;
        }
    }

    public static class PostSmsQueueRequestDto {
        public final DataPayload<PostSmsQueueRequest> data;

        public PostSmsQueueRequestDto(PostSmsQueueRequest postSmsQueueRequest) {
            this.data = new DataPayload<>(postSmsQueueRequest);
        }
    }

}
