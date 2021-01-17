package com.maestro.app.sample.ms.events.repositories;

import com.maestro.app.sample.ms.events.entities.LogConnectEvents;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LogConnectEventsRepository extends CrudRepository<LogConnectEvents, String>, JpaSpecificationExecutor<LogConnectEvents> {
}
