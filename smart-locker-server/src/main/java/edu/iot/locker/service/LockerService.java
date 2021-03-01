package edu.iot.locker.service;

import edu.iot.locker.data.model.LockerEntity;
import edu.iot.locker.data.repository.LockerRepository;
import edu.iot.locker.model.metadata.LockerInfo;
import edu.iot.locker.model.metadata.UserLockerInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockerService {

    UserService userService;
    LockerRepository lockerRepository;

    public LockerEntity findById(Integer id) {
        return lockerRepository.findById(id).orElse(null);
    }

    public List<LockerEntity> findAll() {
        return lockerRepository.findAll();
    }

    public void update(LockerEntity lockerEntity) {
        lockerEntity.setLastModified(LocalDateTime.now());
        lockerRepository.save(lockerEntity);
    }

    public UserLockerInfo toUserInfo(LockerEntity entity) {
        return UserLockerInfo.builder()
                .lockerId(entity.getId())
                .build();
    }

    public LockerInfo toLockerInfo(LockerEntity entity) {
        return LockerInfo.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .ownerInfo(userService.toInfo(entity.getOwner()))
                .currentWeight(entity.getCurrentWeight())
                .maxEndurance(entity.getMaxEndurance())
                .lastModified(entity.getLastModified())
                .build();
    }

    public LockerEntity findFreeLocker() {
        List<LockerEntity> freeLockers = lockerRepository.findByOwnerIsNull();
        if (freeLockers == null || freeLockers.isEmpty()) {
            return null;
        } else {
            return freeLockers.get(0);
        }
    }
}
