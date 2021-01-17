package com.maestro.app.sample.ms.messages;

import com.maestro.app.sample.ms.messages.utils.Constants;
import com.maestro.app.sample.ms.messages.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.queue.QueueBroadcastMessage;
import com.maestro.app.utils.queue.QueueLogPrivateEvt;
import com.maestro.app.utils.queue.QueueUserMessage;
import com.maestro.app.utils.types.QueueEventType;
import com.maestro.app.utils.types.QueueMessageState;
import com.maestro.app.utils.types.TypeAdmin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(initializers = MessagesServiceApplicationTest.Initializer.class)
public class MessagesServiceApplicationTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private Exchange eventExchange;
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    private final static String IdOrg = CommonUtils.generateGuid();

    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    static {
        rabbit.start();
    }

    @Test
    public void testUserMessagesProcessor() throws InterruptedException {
        // push messages
        final int cnt = new Random().nextInt(200);
        for (int i = 0; i < cnt; i++) {
            QueueUserMessage evt = new QueueUserMessage();
            evt.setUser(TestUtils.queryAuthUser("User_" + String.valueOf(i), IdOrg, TypeAdmin.NONE));
            evt.setCode(CommonUtils.generateGuid());
            evt.setState(QueueMessageState.SUCCESS);
            evt.setMessage("EVENT#" + i);
            rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.messages.test", evt);
        }
        // small timeout
        Thread.sleep(5000);
        // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_USERMESSAGE_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_USERMESSAGE_NAME, _queue);

        assertEquals(_queue.getMessageCount(), cnt);

        rabbitListenerEndpointRegistry.getListenerContainer("listener-user-messages").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("listener-user-messages").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_USERMESSAGE_NAME);

        assertEquals(_queue.getMessageCount(), 0);
        assertEquals(_queue.getConsumerCount(), 0);
    }

    @Test
    public void testBroadcastMessagesProcessor() throws InterruptedException {
        // push messages
        final int cnt = new Random().nextInt(200);
        for (int i = 0; i < cnt; i++) {
            QueueBroadcastMessage evt = new QueueBroadcastMessage();
            evt.setState(QueueMessageState.SUCCESS);
            evt.setMessage("EVENT #" + i);
            evt.setTitle("TITLE #" + i);
            rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.broadcast-messages.test", evt);
        }
        // small timeout
        Thread.sleep(5000);
        // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_BROADCASTMESSAGE_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_BROADCASTMESSAGE_NAME, _queue);

        assertEquals(_queue.getMessageCount(), cnt);

        rabbitListenerEndpointRegistry.getListenerContainer("listener-broadcast-messages").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("listener-broadcast-messages").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_BROADCASTMESSAGE_NAME);

        assertEquals(_queue.getMessageCount(), 0);
        assertEquals(_queue.getConsumerCount(), 0);
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}
