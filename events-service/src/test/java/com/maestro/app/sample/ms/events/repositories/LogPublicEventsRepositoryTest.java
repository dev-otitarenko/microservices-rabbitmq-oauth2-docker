package com.maestro.app.sample.ms.events.repositories;

import com.maestro.app.sample.ms.events.entities.LogPublicEvents;
import com.maestro.app.utils.CommonUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogPublicEventsRepositoryTest {
    @Autowired
    private LogPublicEventsRepository logRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String IdEvent;

    @BeforeAll
    public void setup() {
        IdEvent = CommonUtils.generateGuid();
    }

    @AfterAll
    public void stop() {
        jdbcTemplate.update("delete from logs_events_public where id = ?", new Object[]{ IdEvent });
    }

    @Test
    @Order(1)
    @DisplayName("Retrieving the data from the public log")
    public void getLogsPublicRepository () {
        Page<LogPublicEvents> list = logRepository.findAll(null, PageRequest.of(0, 20, Sort.by("dateRec").descending()));

        assertTrue(list.getTotalPages() >= 0);
        assertTrue(list.getTotalElements() >= 0);
        assertTrue(list.getNumberOfElements() >= 0);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println(String.format(" Page info: Count: %d, Pages: %d, Page rows size: %d", list.getTotalElements(), list.getTotalPages(), list.getNumberOfElements()));
        list.getContent().stream().forEach(rdata -> {
            StringBuilder sb = new StringBuilder();
            sb
                .append(" * Processing record: {")
                .append(rdata)
                .append(" }");
            System.out.println(sb.toString());
        });
        stopWatch.stop();

        System.out.println(String.format("Method %s executed within %d miliseconds.", "pageDocuments()", stopWatch.getTotalTimeMillis()));
    }

    @Test
    @Order(0)
    @DisplayName("Saving the event in the public log")
    public void savePublicEvt() {
        LogPublicEvents evt = new LogPublicEvents();
        evt.setCode(IdEvent);
        evt.setMode(1);
        evt.setName("TEST PUBLIC EVENT");
        evt.setIpaddress("127.0.0.1");
        evt.setCountryCode("US");
        evt.setCountryName("USA");
        evt.setCityName("NEW YORK");

        logRepository.save(evt);

        LogPublicEvents savedEvt = logRepository.findById(evt.getCode()).orElse(null);
        assertNotNull(savedEvt);
        assertEquals(savedEvt.getMode(), evt.getMode());
        assertEquals(savedEvt.getIpaddress(), evt.getIpaddress());
        assertEquals(savedEvt.getCountryCode(), evt.getCountryCode());
        assertEquals(savedEvt.getCountryName(), evt.getCountryName());
        assertEquals(savedEvt.getCityName(), evt.getCityName());
    }
}
