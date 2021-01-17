package com.maestro.app.sample.ms.events.services;

import com.maestro.app.sample.ms.events.entities.LogPrivateEvents;
import com.maestro.app.sample.ms.events.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.auth.AuthUser;
import com.maestro.app.utils.queue.QueueLogPrivateEvt;
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
public class LogPrivateEventsServiceTest {
    @Autowired
    private LogPrivateEventsService logService;

    private final String IdAdminUser = CommonUtils.generateGuid();
    private final String IdUser = CommonUtils.generateGuid();
    private final String IdOrg = CommonUtils.generateGuid();

    @Test
    @Order(0)
    public void saveEvent() {
        AuthUser authUser = TestUtils.queryAuthUser(IdUser, IdOrg, TypeAdmin.NONE);

        QueueLogPrivateEvt prm = new QueueLogPrivateEvt();
        prm.setMode(QueueEventType.SIMPLE_MESSAGE);
        prm.setUser(authUser);
        prm.setName("TEST PRIVATE EVENT");

        logService.savePrivateEvt(authUser, prm);
    }

    @Test
    @Order(1)
    public void test_getLogPrivateEvt_IAEAAdmin () {
        AuthUser authUser = TestUtils.queryAuthUser(IdAdminUser, IdOrg, TypeAdmin.SYSTEM_ADMIN);

        Page<LogPrivateEvents> list = logService.getListEvents(authUser, PageRequest.of(0, 20, Sort.by("dateRec").descending()), "");
        assertTrue(list.getTotalPages() > 0);
        assertTrue(list.getTotalElements() > 0);
        assertTrue(list.getNumberOfElements() > 0);
    }
}
