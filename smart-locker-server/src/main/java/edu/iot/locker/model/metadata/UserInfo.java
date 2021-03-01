package edu.iot.locker.model.metadata;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    Integer userId;
    String username;
    String nickName;
}