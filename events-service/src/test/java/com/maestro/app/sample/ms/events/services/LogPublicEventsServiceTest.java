package com.maestro.app.sample.ms.events.services;

import com.maestro.app.sample.ms.events.entities.LogPublicEvents;
import com.maestro.app.sample.ms.events.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.auth.AuthUser;
import com.maestro.app.utils.queue.QueueLogPublicEvt;
import com.maestro.app.utils.types.QueueEventType;
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

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogPublicEventsServiceTest {
    @Autowired
    private LogPublicEventsService logService;

    private String IdAdminUser = CommonUtils.generateGuid();
    private String IdUser = CommonUtils.generateGuid();
    private final String IdOrg = CommonUtils.generateGuid();

    @BeforeAll
    public void setup() {
    }

    @AfterAll
    public void end() {
    }

    @Test
    @Order(0)
    public void savePublicEvt() {
        QueueLogPublicEvt prm = new QueueLogPublicEvt();
        prm.setCity("PARIS");
        prm.setCountryCode("FR");
        prm.setCountryName("FRANCE");
        prm.setIp_address("22.33.11.55");
        prm.setMode(QueueEventType.SIMPLE_MESSAGE);
        prm.setName("TEST PUBLIC EVENT");

        logService.savePublicEvt(prm);
    }

    @Test
    @Order(1)
    public void getLogPublicEvt_UserAdmin () {
        AuthUser authUser = TestUtils.queryAuthUser(IdAdminUser, IdOrg, TypeAdmin.SYSTEM_ADMIN);

        Page<LogPublicEvents> list = logService.getListEvents(authUser, PageRequest.of(0, 20, Sort.by("dateRec").descending()), "");
        assertTrue(list.getTotalPages() > 0);
        assertTrue(list.getTotalElements() > 0);
        assertTrue(list.getNumberOfElements() > 0);
    }
}
