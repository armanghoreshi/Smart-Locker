package edu.iot.locker.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
    String nickname;
    String username;
    String password;
}
