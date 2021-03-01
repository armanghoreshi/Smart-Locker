package edu.iot.locker.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    LoginStatus status;
    String token;

    public enum LoginStatus {
        OK, ERROR, WRONG_PASSWORD, NOT_EXIST
    }
}
