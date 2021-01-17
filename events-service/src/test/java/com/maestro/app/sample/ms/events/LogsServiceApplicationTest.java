package com.maestro.app.sample.ms.events;

import com.maestro.app.sample.ms.events.utils.Constants;
import com.maestro.app.utils.queue.QueueLogPublicEvt;
import com.maestro.app.utils.types.QueueEventType;
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


    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    static {
       rabbit.start();
    }

    @Test
    public void testLogPublicProcessor() throws InterruptedException {
            // push 1 message
        QueueLogPublicEvt evt = new QueueLogPublicEvt();
        evt.setCity("KYIV");
        evt.setCountryCode("UA");
        evt.setCountryName("Ukraine");
        evt.setIp_address("999.999.999.999");
        evt.setMode(QueueEventType.SIMPLE_MESSAGE);
        evt.setName("EVENT#1");
        rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.public.test", evt);
            // push 2 message
        evt = new QueueLogPublicEvt();
        evt.setCity("KYIV");
        evt.setCountryCode("UA");
        evt.setCountryName("Ukraine");
        evt.setIp_address("999.999.999.999");
        evt.setMode(QueueEventType.SIMPLE_MESSAGE);
        evt.setName("EVENT#2");
        rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.public.test", evt);
            // push 3 message
        evt = new QueueLogPublicEvt();
        evt.setCity("KYIV");
        evt.setCountryCode("UA");
        evt.setCountryName("Ukraine");
        evt.setIp_address("999.999.999.999");
        evt.setMode(QueueEventType.SIMPLE_MESSAGE);
        evt.setName("EVENT#3");
        rabbitTemplate.convertAndSend(this.eventExchange.getName(), "events.public.test", evt);
            // small timeout
        Thread.sleep(5000);
            // get information about queue
        QueueInformation _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPUBLIC_NAME);
        log.debug("Queue [{}]: {}", Constants.QUEUE_LOGSPUBLIC_NAME, _queue);

        assertEquals(_queue.getMessageCount(), 3);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-public").start();

        Thread.sleep(5000);

        rabbitListenerEndpointRegistry.getListenerContainer("events-listener-public").stop();

        _queue = amqpAdmin.getQueueInfo(Constants.QUEUE_LOGSPUBLIC_NAME);

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
