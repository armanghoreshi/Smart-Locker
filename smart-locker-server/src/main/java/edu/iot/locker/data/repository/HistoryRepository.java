package edu.iot.locker.data.repository;

import edu.iot.locker.data.model.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {

    List<HistoryEntity> findByLockerId(Integer lockerId);

    List<HistoryEntity> findByUserId(Integer userId);
}
