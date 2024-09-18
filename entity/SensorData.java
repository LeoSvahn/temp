package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorName;
    private double value;
    private LocalDateTime timestamp;

    public SensorData() {}

    public SensorData(String sensorName, double value, LocalDateTime timestamp) {
        this.sensorName = sensorName;
        this.value = value;
        this.timestamp = timestamp;
    }
    // Getter och Setter för ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter och Setter för temperaturvärdet
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // Getter och Setter för tidsstämpel
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
