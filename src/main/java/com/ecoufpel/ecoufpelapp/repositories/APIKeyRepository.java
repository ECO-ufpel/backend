package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface APIKeyRepository extends JpaRepository<APIKey, UUID>{
    Optional<APIKey> findByKey(UUID key);
}
