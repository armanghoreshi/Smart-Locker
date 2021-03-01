package edu.iot.locker.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.iot.locker.model.metadata.LockerInfo;
import edu.iot.locker.model.metadata.UserHistoryInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoResponse {
    Integer id;
    String username;
    String nickName;
    String token;
    LocalDateTime signUpAt;
    LocalDateTime lastLoginAt;
    @JsonIgnoreProperties(value = {"ownerInfo"})
    LockerInfo currentLocker;
    List<UserHistoryInfo> userHistoryInfos;
}
