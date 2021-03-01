package edu.iot.locker.service;

import edu.iot.locker.data.metadata.LockerAction;
import edu.iot.locker.data.model.HistoryEntity;
import edu.iot.locker.data.model.LockerEntity;
import edu.iot.locker.data.model.UserEntity;
import edu.iot.locker.data.repository.HistoryRepository;
import edu.iot.locker.model.metadata.LocketHistoryInfo;
import edu.iot.locker.model.metadata.UserHistoryInfo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserService userService;
    private final LockerService lockerService;

    @Async
    public void addHistory(LockerEntity locker, LockerAction action) {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setLocker(locker);
        historyEntity.setUser(locker.getOwner());
        historyEntity.setAction(action);
        historyEntity.setTime(LocalDateTime.now());
        historyRepository.save(historyEntity);
    }

    public List<LocketHistoryInfo> getLockerHistory(LockerEntity locker) {
        List<HistoryEntity> historyEntities = historyRepository.findByLockerId(locker.getId());
        return historyEntities.parallelStream().map(this::toLockerHistoryInfo).collect(Collectors.toList());
    }

    public List<UserHistoryInfo> getUserHistory(UserEntity user) {
        List<HistoryEntity> historyEntities = historyRepository.findByUserId(user.getId());
        return historyEntities.parallelStream().map(this::toUserHistoryInfo).collect(Collectors.toList());
    }

    private LocketHistoryInfo toLockerHistoryInfo(HistoryEntity entity) {
        return LocketHistoryInfo.builder()
                .userInfo(userService.toInfo(entity.getUser()))
                .action(entity.getAction())
                .time(entity.getTime())
                .build();
    }

    private UserHistoryInfo toUserHistoryInfo(HistoryEntity entity) {
        return UserHistoryInfo.builder()
                .userLockerInfo(lockerService.toUserInfo(entity.getLocker()))
                .action(entity.getAction())
                .time(entity.getTime())
                .build();
    }
}
