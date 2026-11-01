package com.zjgsu.lby.workorder.repository;

import com.zjgsu.lby.workorder.model.WorkOrder;
import com.zjgsu.lby.workorder.model.WorkOrderStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, String> {
    boolean existsByVehicleIdAndStatusIn(String vehicleId, Collection<WorkOrderStatus> statuses);
    List<WorkOrder> findAllByOrderByCreatedAtDesc();
    List<WorkOrder> findByVehicleIdOrderByCreatedAtDesc(String vehicleId);
}
