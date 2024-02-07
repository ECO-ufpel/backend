package com.ecoufpel.ecoufpelapp.websocket;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataConsumptionMessage {
    private DataDTO data;
}
