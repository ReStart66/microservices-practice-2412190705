package com.zjgsu.lby.service;

import com.zjgsu.lby.dto.WorkOrderCreateRequest;
import com.zjgsu.lby.exception.BusinessRuleException;
import com.zjgsu.lby.exception.ResourceNotFoundException;
import com.zjgsu.lby.model.Vehicle;
import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.model.WorkOrderStatus;
import com.zjgsu.lby.repository.VehicleRepository;
import com.zjgsu.lby.repository.WorkOrderRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class WorkOrderService {

    private static final Set<WorkOrderStatus> ACTIVE_STATUSES =
        Set.of(WorkOrderStatus.CREATED, WorkOrderStatus.IN_PROGRESS);

    private final WorkOrderRepository workOrders;
    private final VehicleRepository vehicles;

    public WorkOrderService(WorkOrderRepository workOrders, VehicleRepository vehicles) {
        this.workOrders = workOrders;
        this.vehicles = vehicles;
    }

    @Transactional
    public WorkOrder create(WorkOrderCreateRequest request) {
        Vehicle vehicle = vehicles.findById(request.vehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("车辆不存在: " + request.vehicleId()));

        if (workOrders.existsByVehicle_IdAndStatusIn(vehicle.getId(), ACTIVE_STATUSES)) {
            throw new BusinessRuleException("该车辆已有未完成工单，不能重复创建");
        }

        WorkOrder workOrder = new WorkOrder(
            UUID.randomUUID().toString(),
            vehicle,
            request.serviceAdvisorName().trim(),
            request.technicianName().trim(),
            request.faultDescription().trim()
        );
        return workOrders.save(workOrder);
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> findAll(String vehicleId) {
        if (StringUtils.hasText(vehicleId)) {
            return workOrders.findByVehicle_IdOrderByCreatedAtDesc(vehicleId);
        }
        return workOrders.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public WorkOrder findById(String id) {
        return workOrders.findDetailedById(id)
            .orElseThrow(() -> new ResourceNotFoundException("维修工单不存在: " + id));
    }

    @Transactional
    public WorkOrder complete(String id) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() == WorkOrderStatus.COMPLETED) {
            throw new BusinessRuleException("维修工单已经完成，不能重复完成: " + id);
        }
        workOrder.complete();
        return workOrders.save(workOrder);
    }
}
