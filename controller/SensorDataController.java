package com.example.demo.controller;

import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SensorDataController {

    private final SensorDataRepository sensorDataRepository;

    // Injektera repository
    public SensorDataController(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    // Endpoint för att hämta all sensordata
    @GetMapping("/get-sensor-data")
    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }
}
