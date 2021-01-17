package com.maestro.app.sample.ms.messages.listeners;

import com.maestro.app.sample.ms.messages.services.UserMessagesService;
import com.maestro.app.sample.ms.messages.utils.Constants;
import com.maestro.app.utils.queue.QueueBroadcastMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = Constants.QUEUE_BROADCASTMESSAGE_NAME)
public class BroadcastMessagesProcessor {
    private final UserMessagesService messageService;

    public BroadcastMessagesProcessor(UserMessagesService messageService) {
        this.messageService = messageService;
    }

    @RabbitHandler
    public void processEvent(final QueueBroadcastMessage event) {
        log.debug(" -> [UserMessageProcessor]  message [{}] received", event);
        messageService.saveMessage(event);
    }
}
