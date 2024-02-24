package com.ecoufpel.ecoufpelapp.repositories;

import com.ecoufpel.ecoufpelapp.domains.sensor.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface APIKeyRepository extends JpaRepository<APIKey, UUID>{
    Optional<APIKey> findByKey(UUID key);
}
