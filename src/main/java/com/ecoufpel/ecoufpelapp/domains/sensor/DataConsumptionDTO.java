package com.ecoufpel.ecoufpelapp.domains.sensor;

import java.math.BigInteger;
import java.sql.Timestamp;

public record DataConsumptionDTO(String classroom_id, Timestamp date_time, double consumption) {
}
