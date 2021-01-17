package com.maestro.app.sample.ms.events.repositories;

import com.maestro.app.sample.ms.events.entities.LogPrivateEvents;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LogPrivateEventsRepository extends CrudRepository<LogPrivateEvents, String>, JpaSpecificationExecutor<LogPrivateEvents> {
}
