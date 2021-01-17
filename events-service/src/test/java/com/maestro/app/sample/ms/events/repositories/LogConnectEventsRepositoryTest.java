package com.maestro.app.sample.ms.events.repositories;

import com.maestro.app.sample.ms.events.entities.LogConnectEvents;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogConnectEventsRepositoryTest {
    @Autowired
    private LogConnectEventsRepository logRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String IdEvent = "";

    @BeforeAll
    public void setup() {
        IdEvent = CommonUtils.generateGuid();
    }

    @AfterAll
    public void end() {
        jdbcTemplate.update("delete from logs_connects where id = ?", new Object[]{ IdEvent });
    }

    @Test
    @Order(0)
    @DisplayName("Saving a record in the connects log")
    void saveEvent() {
        LogConnectEvents evt = new LogConnectEvents();
        evt.setCode(IdEvent);
        evt.setIduser("*");
        evt.setUsername("TEST_USER_" + IdEvent);
        evt.setIpaddress("127.127.127.127");
        evt.setCountryCode("UA");
        evt.setCountryName("UKRAINE");
        evt.setCityName("KOROSTEN'");
        evt.setDeviceDetails("IPhone 11 under Ubuntu");
        evt.setDateRec(new Date());

        this.logRepository.save(evt);

        LogConnectEvents evt1 = logRepository.findById(IdEvent).orElse(null);
        assertNotNull(evt1);
        assertEquals(evt.getCode(), evt1.getCode());
        assertEquals(evt.getIduser(), evt1.getIduser());
        assertEquals(evt.getUsername(), evt1.getUsername());
        assertEquals(evt.getIpaddress(), evt1.getIpaddress());
        assertEquals(evt.getCountryCode(), evt1.getCountryCode());
        assertEquals(evt.getCountryName(), evt1.getCountryName());
        assertEquals(evt.getCityName(), evt1.getCityName());
    }

    @Test
    @Order(1)
    @DisplayName("Retrieving the data from the connects log")
    void getLogsConnects () {
        Page<LogConnectEvents> list = logRepository.findAll(null, PageRequest.of(0, 20, Sort.by("dateRec").descending()));

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

        System.out.println(String.format("Method %s executed within $d miliseconds.", "pageDocuments()", stopWatch.getTotalTimeMillis()));
    }
}
