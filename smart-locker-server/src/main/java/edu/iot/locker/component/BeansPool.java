package edu.iot.locker.component;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeansPool {
    public final static String LOCKER_STATUS_QUEUE = "locker-status";
    public final static String LOCKER_JOB_QUEUE = "locker-job";

    @Bean
    public Queue lockerStatusQueue() {
        return new Queue(LOCKER_STATUS_QUEUE);
    }

    @Bean
    public Queue lockerJobQueue() {
        return new Queue(LOCKER_JOB_QUEUE);
    }

}
