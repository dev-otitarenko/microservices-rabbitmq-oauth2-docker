package com.maestro.app.sample1.ms.auth.repositories;

import com.maestro.app.sample1.ms.auth.entities.DeviceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, String> {
    List<DeviceMetadata> findByUserId(String userId);
}
