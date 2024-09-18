package com.example.demo.repository;

import com.example.demo.entity.SensorData;  // Rätt import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    // Här kan du lägga till egna metoder om du behöver, annars räcker de inbyggda metoderna som save, findAll etc.
}
