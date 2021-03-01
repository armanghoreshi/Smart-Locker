package edu.iot.locker.model.response;

import edu.iot.locker.data.metadata.LockerStatus;
import edu.iot.locker.model.metadata.LocketHistoryInfo;
import edu.iot.locker.model.metadata.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockerInfoResponse {
    Integer lockerId;
    LockerStatus lockerStatus;
    UserInfo currentOwner;
    Double currentWeight;
    Double maxEndurance;
    LocalDateTime lastModified;
    List<LocketHistoryInfo> locketHistoryInfos;
}
