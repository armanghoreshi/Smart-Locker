package edu.iot.locker.data.repository;

import edu.iot.locker.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByToken(String token);

    Optional<UserEntity> findByUsername(String username);
}
