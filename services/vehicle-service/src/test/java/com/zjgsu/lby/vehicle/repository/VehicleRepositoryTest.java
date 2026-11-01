package com.zjgsu.lby.vehicle.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.vehicle.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class VehicleRepositoryTest {
    @Autowired VehicleRepository vehicles;

    @Test
    void savesAndFindsByPlateNumber() {
        vehicles.saveAndFlush(new Vehicle("v1", "Tester", "V001", "Brand", "Model", 100));
        assertEquals("Tester", vehicles.findByPlateNumber("V001").orElseThrow().getOwnerName());
    }

    @Test
    void enforcesUniquePlateNumber() {
        vehicles.saveAndFlush(new Vehicle("v1", "Tester", "V001", "Brand", "Model", 100));
        assertThrows(DataIntegrityViolationException.class,
            () -> vehicles.saveAndFlush(new Vehicle("v2", "Other", "V001", "Brand", "Model", 200)));
    }
}
