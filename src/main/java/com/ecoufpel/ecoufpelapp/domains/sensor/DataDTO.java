package com.ecoufpel.ecoufpelapp.domains.sensor;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public record DataDTO(BigInteger room_id, Timestamp time, double consumption) {
}
