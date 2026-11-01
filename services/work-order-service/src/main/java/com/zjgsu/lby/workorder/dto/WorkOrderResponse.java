package com.zjgsu.lby.workorder.dto;

import com.zjgsu.lby.workorder.model.WorkOrder;
import com.zjgsu.lby.workorder.model.WorkOrderStatus;
import java.time.Instant;

public record WorkOrderResponse(String id, String vehicleId, String partId, int partQuantity,
                                String serviceAdvisorName, String technicianName,
                                String faultDescription, WorkOrderStatus status,
                                Instant createdAt, Instant completedAt) {
    public static WorkOrderResponse from(WorkOrder workOrder) {
        return new WorkOrderResponse(workOrder.getId(), workOrder.getVehicleId(), workOrder.getPartId(),
            workOrder.getPartQuantity(), workOrder.getServiceAdvisorName(), workOrder.getTechnicianName(),
            workOrder.getFaultDescription(), workOrder.getStatus(), workOrder.getCreatedAt(),
            workOrder.getCompletedAt());
    }
}
