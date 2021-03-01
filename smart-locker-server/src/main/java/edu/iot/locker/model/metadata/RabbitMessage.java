package edu.iot.locker.model.metadata;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMessage {
    String type;
    String data;
}
