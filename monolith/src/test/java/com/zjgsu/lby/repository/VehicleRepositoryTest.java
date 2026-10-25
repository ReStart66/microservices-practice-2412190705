package com.zjgsu.lby.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.model.Vehicle;
import com.zjgsu.lby.model.WorkOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    VehicleRepository vehicles;

    @Autowired
    WorkOrderRepository workOrders;

    @Test
    void savesAndFindsVehicleByPlateNumber() {
        Vehicle vehicle = vehicles.saveAndFlush(new Vehicle(
            "vehicle-1", "陆兵尧", "浙A12345", "大众", "迈腾", 52000
        ));

        assertEquals("陆兵尧", vehicles.findByPlateNumber(vehicle.getPlateNumber()).orElseThrow().getOwnerName());
    }

    @Test
    void enforcesUniquePlateNumber() {
        vehicles.saveAndFlush(new Vehicle("vehicle-1", "陆兵尧", "浙A12345", "大众", "迈腾", 52000));

        assertThrows(DataIntegrityViolationException.class, () -> vehicles.saveAndFlush(
            new Vehicle("vehicle-2", "张三", "浙A12345", "丰田", "凯美瑞", 30000)
        ));
    }

    @Test
    void savesWorkOrderWithVehicleRelation() {
        Vehicle vehicle = vehicles.saveAndFlush(new Vehicle(
            "vehicle-1", "陆兵尧", "浙A12345", "大众", "迈腾", 52000
        ));
        workOrders.saveAndFlush(new WorkOrder(
            "work-order-1", vehicle, "王顾问", "李技师", "发动机异响"
        ));

        assertEquals(1, workOrders.findByVehicle_IdOrderByCreatedAtDesc(vehicle.getId()).size());
    }
}
