package com.zjgsu.lby.workorder.service;

import com.zjgsu.lby.workorder.client.RemoteServiceClient;
import com.zjgsu.lby.workorder.dto.WorkOrderCreateRequest;
import com.zjgsu.lby.workorder.exception.BusinessRuleException;
import com.zjgsu.lby.workorder.exception.ResourceNotFoundException;
import com.zjgsu.lby.workorder.model.WorkOrder;
import com.zjgsu.lby.workorder.model.WorkOrderStatus;
import com.zjgsu.lby.workorder.repository.WorkOrderRepository;
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
    private final RemoteServiceClient remote;

    public WorkOrderService(WorkOrderRepository workOrders, RemoteServiceClient remote) {
        this.workOrders = workOrders;
        this.remote = remote;
    }

    @Transactional
    public WorkOrder create(WorkOrderCreateRequest request) {
        if (workOrders.existsByVehicleIdAndStatusIn(request.vehicleId(), ACTIVE_STATUSES)) {
            throw new BusinessRuleException("该车辆已有未完成工单，不能重复创建");
        }
        remote.validateVehicle(request.vehicleId());
        remote.reservePart(request.partId(), request.partQuantity());
        return workOrders.save(new WorkOrder(UUID.randomUUID().toString(), request.vehicleId(),
            request.partId(), request.partQuantity(), request.serviceAdvisorName().trim(),
            request.technicianName().trim(), request.faultDescription().trim()));
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> findAll(String vehicleId) {
        if (StringUtils.hasText(vehicleId)) {
            return workOrders.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
        }
        return workOrders.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public WorkOrder findById(String id) {
        return workOrders.findById(id)
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
