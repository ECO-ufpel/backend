package com.ecoufpel.ecoufpelapp.domains.sensor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "api_keys", schema = "sensor_data")
@Getter
@Setter
public class APIKey {
    @Id
    @GeneratedValue
    private UUID key;
    @NonNull
    private Date expiration;
    private boolean active = true;
    private Timestamp lastUsed = null;
}
