package com.zjgsu.lby.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.dto.VehicleCreateRequest;
import com.zjgsu.lby.dto.WorkOrderCreateRequest;
import com.zjgsu.lby.exception.BusinessRuleException;
import com.zjgsu.lby.exception.ResourceNotFoundException;
import com.zjgsu.lby.model.Vehicle;
import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.model.WorkOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WorkOrderServiceTest {

    @Autowired
    VehicleService vehicles;

    @Autowired
    WorkOrderService workOrders;

    @Test
    void rejectsSecondActiveWorkOrderAndAllowsOneAfterCompletion() {
        Vehicle vehicle = createVehicle("浙A90001");
        WorkOrder first = workOrders.create(new WorkOrderCreateRequest(
            vehicle.getId(), "王顾问", "李技师", "发动机异响"
        ));

        assertEquals(WorkOrderStatus.CREATED, first.getStatus());
        assertThrows(BusinessRuleException.class, () -> workOrders.create(new WorkOrderCreateRequest(
            vehicle.getId(), "王顾问", "赵技师", "重复创建"
        )));

        WorkOrder completed = workOrders.complete(first.getId());
        assertEquals(WorkOrderStatus.COMPLETED, completed.getStatus());

        WorkOrder second = workOrders.create(new WorkOrderCreateRequest(
            vehicle.getId(), "王顾问", "赵技师", "第二次维修"
        ));
        assertEquals(WorkOrderStatus.CREATED, second.getStatus());
    }

    @Test
    void rejectsUnknownVehicle() {
        assertThrows(ResourceNotFoundException.class, () -> workOrders.create(new WorkOrderCreateRequest(
            "missing-vehicle", "王顾问", "李技师", "车辆不存在"
        )));
    }

    @Test
    void listsWorkOrdersByVehicle() {
        Vehicle vehicle = createVehicle("浙A90002");
        workOrders.create(new WorkOrderCreateRequest(
            vehicle.getId(), "王顾问", "李技师", "刹车异响"
        ));

        assertEquals(1, workOrders.findAll(vehicle.getId()).size());
    }

    private Vehicle createVehicle(String plateNumber) {
        return vehicles.create(new VehicleCreateRequest(
            "陆兵尧", plateNumber, "大众", "迈腾", 52000
        ));
    }
}
