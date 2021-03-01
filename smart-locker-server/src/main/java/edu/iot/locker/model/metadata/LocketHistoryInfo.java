package edu.iot.locker.model.metadata;

import edu.iot.locker.data.metadata.LockerAction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocketHistoryInfo {
    UserInfo userInfo;
    LockerAction action;
    LocalDateTime time;
}
