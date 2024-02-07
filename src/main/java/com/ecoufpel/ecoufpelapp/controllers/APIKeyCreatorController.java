package com.ecoufpel.ecoufpelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecoufpel.ecoufpelapp.repositories.APIKeyRepository;
import com.ecoufpel.ecoufpelapp.domains.sensor.APIKey;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class APIKeyCreatorController {
    private static final int DEFAULT_EXPIRATION_DAYS = 90;

    @Autowired
    private APIKeyRepository apiKeyRepository;

    @PostMapping("/create")
    public ResponseEntity createAPIKey() {
        var key = new APIKey();
        key.setExpiration(Date.valueOf(LocalDate.now().plusDays(DEFAULT_EXPIRATION_DAYS)));
        apiKeyRepository.save(key);
        return ResponseEntity.status(HttpStatus.OK).body(key.getKey().toString());
    }

    @PostMapping("/revoke")
    public ResponseEntity revokeAPIKey(@RequestBody UUID key) {
        // Revoke an API key from the database
        var obj_key = apiKeyRepository.findByKey(key);
        if (obj_key.isPresent()) {
            var keyObj = obj_key.get();
            keyObj.setActive(false);
            apiKeyRepository.save(keyObj);
            return ResponseEntity.status(HttpStatus.OK).body("API Key revoked");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API Key not found");
    }

    @PostMapping("/renew")
    public ResponseEntity renewAPIKey(@RequestBody UUID key) {
        // Renew an API key from the database
        var obj_key = apiKeyRepository.findByKey(key);
        if (obj_key.isPresent()) {
            var keyObj = obj_key.get();
            keyObj.setExpiration(Date.valueOf(LocalDate.now().plusDays(DEFAULT_EXPIRATION_DAYS)));
            keyObj.setActive(true);
            apiKeyRepository.save(keyObj);
            return ResponseEntity.status(HttpStatus.OK).body("API Key renewed");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API Key not found");
    }

}
