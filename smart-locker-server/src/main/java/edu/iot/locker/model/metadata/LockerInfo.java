package edu.iot.locker.model.metadata;

import edu.iot.locker.data.metadata.LockerStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockerInfo {
    Integer id;
    LockerStatus status;
    UserInfo ownerInfo;
    Double currentWeight;
    Double maxEndurance;
    LocalDateTime lastModified;
}
