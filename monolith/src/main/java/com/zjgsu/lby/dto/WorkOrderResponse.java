package com.zjgsu.lby.dto;

import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.model.WorkOrderStatus;
import java.time.Instant;

public record WorkOrderResponse(
    String id,
    String vehicleId,
    String plateNumber,
    String serviceAdvisorName,
    String technicianName,
    String faultDescription,
    WorkOrderStatus status,
    Instant createdAt,
    Instant completedAt
) {
    public static WorkOrderResponse from(WorkOrder workOrder) {
        return new WorkOrderResponse(
            workOrder.getId(),
            workOrder.getVehicle().getId(),
            workOrder.getVehicle().getPlateNumber(),
            workOrder.getServiceAdvisorName(),
            workOrder.getTechnicianName(),
            workOrder.getFaultDescription(),
            workOrder.getStatus(),
            workOrder.getCreatedAt(),
            workOrder.getCompletedAt()
        );
    }
}
