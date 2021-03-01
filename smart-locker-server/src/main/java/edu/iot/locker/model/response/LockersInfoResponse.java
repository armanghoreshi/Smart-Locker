package edu.iot.locker.model.response;

import edu.iot.locker.model.metadata.LockerInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockersInfoResponse {
    List<LockerInfo> lockersInfo;
}
