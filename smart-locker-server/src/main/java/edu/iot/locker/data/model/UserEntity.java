package edu.iot.locker.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"", schema = "iot")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    String username;
    String password;
    String nickName;
    String token;
    LocalDateTime signUpAt;
    LocalDateTime lastLoginAt;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "owner_id")
    LockerEntity locker;
}
