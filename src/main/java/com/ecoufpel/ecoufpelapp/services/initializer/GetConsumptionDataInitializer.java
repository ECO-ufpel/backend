package com.ecoufpel.ecoufpelapp.services.initializer;

import com.ecoufpel.ecoufpelapp.services.GetConsumptionDataService;
import com.ecoufpel.ecoufpelapp.websocket.WebSocketEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GetConsumptionDataInitializer implements ApplicationRunner{

    private final GetConsumptionDataService getConsumptionDataService;

    @Autowired
    public GetConsumptionDataInitializer(GetConsumptionDataService getConsumptionDataService, WebSocketEventListener webSocketEventListener){
        this.getConsumptionDataService = getConsumptionDataService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        getConsumptionDataService.initialize();
    }
}
