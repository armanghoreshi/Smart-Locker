package edu.iot.locker.data.model;

import edu.iot.locker.data.metadata.LockerStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locker", schema = "iot")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockerEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    LockerStatus status;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    UserEntity owner;

    Double currentWeight;
    Double maxEndurance;

    LocalDateTime lastModified;

}
