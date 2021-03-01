package edu.iot.locker.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class RMQSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendOnRabbit(String message) {
        rabbitTemplate.convertAndSend(BeansPool.LOCKER_JOB_QUEUE, message);
    }
}
