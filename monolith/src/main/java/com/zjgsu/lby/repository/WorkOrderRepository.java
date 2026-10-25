package com.zjgsu.lby.repository;

import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.model.WorkOrderStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, String> {

    boolean existsByVehicle_IdAndStatusIn(String vehicleId, Collection<WorkOrderStatus> statuses);

    @EntityGraph(attributePaths = "vehicle")
    List<WorkOrder> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "vehicle")
    List<WorkOrder> findByVehicle_IdOrderByCreatedAtDesc(String vehicleId);

    @Query("select w from WorkOrder w join fetch w.vehicle where w.id = :id")
    Optional<WorkOrder> findDetailedById(@Param("id") String id);
}
