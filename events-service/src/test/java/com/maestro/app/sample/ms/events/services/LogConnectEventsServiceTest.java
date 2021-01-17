package com.maestro.app.sample.ms.events.services;

import com.maestro.app.sample.ms.events.entities.LogConnectEvents;
import com.maestro.app.sample.ms.events.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.auth.AuthUser;
import com.maestro.app.utils.queue.QueueLogConnectEvt;
import com.maestro.app.utils.types.TypeAdmin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by O.Titarenko@iaea.org on Sep-2020.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogConnectEventsServiceTest {
    @Autowired
    private LogConnectEventsService logService;

    private final String IdAdminUser = CommonUtils.generateGuid();
    private final String IdUser = CommonUtils.generateGuid();
    private final String IdOrg = CommonUtils.generateGuid();

    @BeforeAll
    public void setup() {
    }

    @AfterAll
    public void end() {
    }

    @Test
    @Order(0)
    public void saveEvent() {
        AuthUser authUser = TestUtils.queryAuthUser(IdUser, IdOrg, TypeAdmin.NONE);

        QueueLogConnectEvt evt = new QueueLogConnectEvt();
        evt.setIduser(authUser.getId());
        evt.setUsername(authUser.getUsername());
        evt.setIp_address("11.22.33.44");
        evt.setDeviceDetails("TEST_DEV_DEVICE");
        evt.setCity("HAMBURG");
        evt.setCountryCode("DE");
        evt.setCountryName("GERMAN");

        logService.saveConnectEvt(evt);
    }

    @Test
    @Order(1)
    public void getLogConnects_IAEAAdmin () {
        AuthUser authUser = TestUtils.queryAuthUser(IdAdminUser, IdOrg, TypeAdmin.SYSTEM_ADMIN);

        Page<LogConnectEvents> list = logService.getListEvents(authUser, PageRequest.of(0, 20, Sort.by("dateRec").descending()), "");
        assertTrue(list.getTotalPages() >= 0);
        assertTrue(list.getTotalElements() >= 0);
        assertTrue(list.getNumberOfElements() >= 0);
    }
}
