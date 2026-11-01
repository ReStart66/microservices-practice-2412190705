package com.zjgsu.lby.workorder.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zjgsu.lby.workorder.model.WorkOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class WorkOrderRepositoryTest {
    @Autowired WorkOrderRepository workOrders;

    @Test
    void savesAndFindsByVehicle() {
        workOrders.saveAndFlush(new WorkOrder("w1", "v1", "p1", 2,
            "Advisor", "Technician", "Engine noise"));
        assertEquals(1, workOrders.findByVehicleIdOrderByCreatedAtDesc("v1").size());
    }
}
