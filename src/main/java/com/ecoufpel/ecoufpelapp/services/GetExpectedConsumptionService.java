package com.ecoufpel.ecoufpelapp.services;

import com.ecoufpel.ecoufpelapp.domains.sensor.DataConsumption;
import com.ecoufpel.ecoufpelapp.domains.websocket.ExpectedConsumptionMessageDTO;
import com.ecoufpel.ecoufpelapp.repositories.DataConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class GetExpectedConsumptionService {
    private final HashMap<String, List<WebSocketSession>> rooms_connected = new HashMap<>();
    private final Timer timer;
    private final DataConsumptionRepository dataConsumptionRepository;

    @Autowired
    public GetExpectedConsumptionService(DataConsumptionRepository dataConsumptionRepository) {
        this.timer = new Timer();
        this.dataConsumptionRepository = dataConsumptionRepository;
        this.timer.scheduleAtFixedRate(new GetExpectedConsumptionTask(), 0, 1000 * 60 * 60);
    }
    public void register_user(String room_id, WebSocketSession client) {
        var list = rooms_connected.get(room_id);

        if (list == null) {
            list = new ArrayList<>();
            list.add(client);
            rooms_connected.put(room_id, list);
        } else {
            list.add(client);
        }
        var expectedConsumption = createExpectedConsumptionMessage(room_id);
        try {
            client.sendMessage(expectedConsumption.toTextMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ExpectedConsumptionMessageDTO createExpectedConsumptionMessage(String room_id) {
        var pair = getStdDevAndAverage(room_id);
        ExpectedConsumptionMessageDTO expectedConsumption;
        if (pair.isPresent()){
            var std_dev = pair.get().getSecond();
            var mean = pair.get().getFirst();
            expectedConsumption = new ExpectedConsumptionMessageDTO(room_id, mean, std_dev);
        }
        else {
            expectedConsumption = new ExpectedConsumptionMessageDTO(room_id, null, null);
        }
        return expectedConsumption;
    }

    public void remove_user(String room_id, WebSocketSession client) {
        var list = rooms_connected.get(room_id);

        if (list != null) {
            list.remove(client);
        }
    }

    public void remove_user(WebSocketSession client) {
        rooms_connected.forEach((room_id, list) -> {
            list.remove(client);
        });
    }

    public class GetExpectedConsumptionTask extends TimerTask {
        @Override
        public void run() {
            rooms_connected.forEach((room_id, list) -> {
                var expectedConsumption = createExpectedConsumptionMessage(room_id);
                list.forEach(client -> {
                    try {
                        client.sendMessage(expectedConsumption.toTextMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

    }
    private static Optional<Double> calculateAverage(List<DataConsumption> values) {
        if (values.isEmpty()) {
            return Optional.empty();
        }
        var mean = values
                .stream()
                .map(DataConsumption::getConsumption)
                .reduce(Double::sum)
                .orElse(0.0) / values.size();

        return Optional.of(mean);
    }

    private static Optional<Double> calculateStdDev(List<DataConsumption> values, Double average) {
        if (values.isEmpty()) {
            return Optional.empty();
        }
        double standardDeviation = 0.0;
        int length = values.size();

        for(DataConsumption num: values) {
            standardDeviation += Math.pow(num.getConsumption() - average, 2);
        }

        return Optional.of(Math.sqrt(standardDeviation/length));
    }

    private Optional<Pair<Double, Double>> getStdDevAndAverage(String room_id) {
        var now = Instant.now();
        var oneHourBefore = now.minus(Duration.ofHours(1));
        var consumptions = dataConsumptionRepository.getLastHourConsumptionByRoomId(room_id, Timestamp.from(oneHourBefore), Timestamp.from(now));
        var mean = calculateAverage(consumptions);
        if (mean.isPresent()) {
            var stdDev = calculateStdDev(consumptions, mean.get());
            return Optional.of(Pair.of(mean.get(), stdDev.orElse(null)));
        }
        return Optional.empty();
    }
}
