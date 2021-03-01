package edu.iot.locker.data.repository;

import edu.iot.locker.data.model.LockerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerRepository extends JpaRepository<LockerEntity, Integer> {

    List<LockerEntity> findByOwnerIsNull();
}
