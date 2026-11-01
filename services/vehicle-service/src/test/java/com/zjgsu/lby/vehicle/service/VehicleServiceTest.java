package com.zjgsu.lby.vehicle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.vehicle.dto.VehicleCreateRequest;
import com.zjgsu.lby.vehicle.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VehicleServiceTest {
    @Autowired VehicleService vehicles;

    @Test
    void createsAndRejectsDuplicatePlate() {
        vehicles.create(new VehicleCreateRequest("Tester", "v-001", "Brand", "Model", 100));
        assertEquals(1, vehicles.findAll().size());
        assertThrows(BusinessRuleException.class,
            () -> vehicles.create(new VehicleCreateRequest("Other", "V-001", "Brand", "Model", 200)));
    }
}
