package edu.iot.locker.business;

import edu.iot.locker.controller.LockerController;
import edu.iot.locker.model.metadata.RabbitMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitHandler {

    LockerController lockerController;

    public boolean handleMessage(RabbitMessage rabbitMessage) {
        switch (rabbitMessage.getType()) {
            case "close-locker":
                String lockerId = rabbitMessage.getData();
                return lockerController.closeLocker(lockerId);
        }

        log.error("Unknown rabbit message: {}!", rabbitMessage.getType());
        return false;
    }
}
