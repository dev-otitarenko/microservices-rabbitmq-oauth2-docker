package com.maestro.app.sample.ms.events;

import com.maestro.app.sample.ms.events.utils.Constants;
import com.maestro.app.sample.ms.events.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.queue.QueueLogConnectEvt;
import com.maestro.app.utils.queue.QueueLogPrivateEvt;
import com.maestro.app.utils.queue.QueueLogPublicEvt;
import com.maestro.app.utils.types.QueueEventType;
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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = LogsServiceApplicationTest.Initializer.class)
public class LogsServiceApplicationTest {
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
    public void testLogPublicProcessor() throws InterruptedException {
            // push messages
        final int cnt = new Random().nextInt(200);
        for (int i = 0; i < cnt; i++) {
            QueueLogPublicEvt evt = new QueueLogPublicEvt();
            evt.setCity("LION");
            evt.setCountryCode("FR");
            evt.setCountryName("France");
            evt.setIp_address("2.3.3." + cnt);
            evt.setMode(QueueEventType.SIMPLE_MESSAGE);
            evt.setName("EVENT#" + i);
            rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.public.test", evt);
        }
            // small timeout
        Thread.sleep(5000);
            // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPUBLIC_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_LOGSPUBLIC_NAME, _queue);

        assertEquals(_queue.getMessageCount(), cnt);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-public").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-public").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPUBLIC_NAME);

        assertEquals(_queue.getMessageCount(), 0);
        assertEquals(_queue.getConsumerCount(), 0);
    }

    @Test
    public void testLogPrivateProcessor() throws InterruptedException {
        // push messages
        final int cnt = new Random().nextInt(200);
        for (int i = 0; i < cnt; i++) {
            QueueLogPrivateEvt evt = new QueueLogPrivateEvt();
            evt.setUser(TestUtils.queryAuthUser("User_" + String.valueOf(i), IdOrg, TypeAdmin.NONE));
            evt.setMode(QueueEventType.SIMPLE_MESSAGE);
            evt.setName("EVENT#" + i);
            rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.private.test", evt);
        }
        // small timeout
        Thread.sleep(5000);
        // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPRIVATE_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_LOGSPRIVATE_NAME, _queue);

        assertEquals(_queue.getMessageCount(), cnt);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-private").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-private").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPRIVATE_NAME);

        assertEquals(_queue.getMessageCount(), 0);
        assertEquals(_queue.getConsumerCount(), 0);
    }

    @Test
    public void testLogConnectsProcessor() throws InterruptedException {
        // push messages
        final int cnt = new Random().nextInt(200);
        for (int i = 0; i < cnt; i++) {
            QueueLogConnectEvt evt = new QueueLogConnectEvt();
            evt.setIduser("User_" + i);
            evt.setUsername("User #" + i);
            evt.setCity("MUNICH");
            evt.setCountryCode("DE");
            evt.setCountryName("German");
            evt.setIp_address("5.5.23." + cnt);
            rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.connects.test", evt);
        }
        // small timeout
        Thread.sleep(5000);
        // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSCONNECTS_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_LOGSCONNECTS_NAME, _queue);

        assertEquals(_queue.getMessageCount(), cnt);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-connects").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-connects").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSCONNECTS_NAME);

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
