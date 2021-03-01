package edu.iot.locker.controller;

import edu.iot.locker.component.RMQSender;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class ShellController {

    private final RMQSender rmqSender;

    @ShellMethod(key = "lock", value = "lock locker")
    private void lock(int lockerId) {
        rmqSender.sendOnRabbit("ON");
    }

    @ShellMethod(key = "unlock", value = "unlock locker")
    private void unlock(int lockerId) {
        rmqSender.sendOnRabbit("OFF");
    }
}
