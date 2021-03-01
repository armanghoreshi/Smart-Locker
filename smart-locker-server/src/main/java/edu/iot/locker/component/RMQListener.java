package edu.iot.locker.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import edu.iot.locker.business.RabbitHandler;
import edu.iot.locker.model.metadata.RabbitMessage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RMQListener {

    RabbitHandler rabbitHandler;
    ObjectReader rabbitMessageReader;

    public RMQListener(RabbitHandler rabbitHandler, ObjectMapper objectMapper) {
        this.rabbitHandler = rabbitHandler;
        this.rabbitMessageReader = objectMapper.reader().forType(RabbitMessage.class);
    }

    @RabbitListener(queues = BeansPool.LOCKER_STATUS_QUEUE)
    private boolean messageHandler(Message message) {
        byte[] body = message.getBody();
        String messageStr = new String(body);
        log.info("Message received from server on rabbit: {}", messageStr);

        try {
            RabbitMessage rabbitMessage = rabbitMessageReader.readValue(messageStr);
            return rabbitHandler.handleMessage(rabbitMessage);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing rabbit message!", e);
            return false;
        }
    }
}
