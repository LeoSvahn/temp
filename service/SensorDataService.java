package com.example.demo.service;

import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;
import com.example.demo.serial.SerialCommunicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SerialCommunicationService serialCommunicationService;

    public SensorDataService(SensorDataRepository sensorDataRepository, SerialCommunicationService serialCommunicationService) {
        this.sensorDataRepository = sensorDataRepository;
        this.serialCommunicationService = serialCommunicationService;
    }

    // Schemalägg insamling var 5:e sekund
    @Scheduled(fixedRate = 5000)
    public void collectAndStoreSensorData() {
        try {
            // Öppna seriell port
            serialCommunicationService.openSerialPort();

            // Läs data från seriell port
            String data = serialCommunicationService.readSensorData();

            // Extrahera temperatur från den mottagna datan
            double temperature = serialCommunicationService.extractTemperature(data);

            // Skapa nytt sensorobjekt och lagra i databasen
            SensorData sensorData = new SensorData("Arduino", temperature, LocalDateTime.now());
            sensorDataRepository.save(sensorData);

            // Logga den lagrade datan
            System.out.println("Sensordata lagrad: " + temperature);

        } catch (Exception e) {
            System.err.println("Fel vid insamling eller lagring av sensordata: " + e.getMessage());
        } finally {
            // Stäng seriell port, även vid fel
            serialCommunicationService.closeSerialPort();
        }
    }
}