package edu.iot.locker.data.model;

import edu.iot.locker.data.metadata.LockerAction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history", schema = "iot")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @OneToOne
    @JoinColumn(name = "locker_id", referencedColumnName = "id")
    LockerEntity locker;

    @Enumerated(EnumType.STRING)
    LockerAction action;

    LocalDateTime time;

}
