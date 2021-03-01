package edu.iot.locker.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpResponse {
    SignUpStatus status;
    String token;

    public enum SignUpStatus {
        OK, ERROR
    }
}
