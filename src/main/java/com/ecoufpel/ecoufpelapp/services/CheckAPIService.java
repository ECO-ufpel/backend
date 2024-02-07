package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.repositories.APIKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class CheckAPIService {
    private final APIKeyRepository apiKeyRepository;

    public CheckAPIService(APIKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public boolean checkAPIKey(UUID key) {
        var api_key = apiKeyRepository.findByKey(key);
        if (api_key.isPresent()) {
            var keyObj = api_key.get();
            if (keyObj.isActive() && keyObj.getExpiration().after(new Date())) {
                keyObj.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
                apiKeyRepository.save(keyObj);
                return true;
            }
        }

        return false;
    }

}
