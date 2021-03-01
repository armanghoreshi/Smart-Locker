package edu.iot.locker.service;

import edu.iot.locker.data.model.UserEntity;
import edu.iot.locker.data.repository.UserRepository;
import edu.iot.locker.model.metadata.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserEntity addUser(UserEntity entity) {
        return userRepository.save(entity);
    }

    public void update(UserEntity entity) {
        userRepository.save(entity);
    }

    public UserInfo toInfo(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserInfo.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .nickName(entity.getNickName())
                .build();
    }
}
