package com.zjgsu.lby.workorder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "work_orders", indexes = @Index(name = "idx_work_orders_vehicle_status", columnList = "vehicle_id,status"))
public class WorkOrder {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(name = "vehicle_id", length = 36, nullable = false)
    private String vehicleId;
    @Column(name = "part_id", length = 36, nullable = false)
    private String partId;
    @Column(name = "part_quantity", nullable = false)
    private int partQuantity;
    @Column(name = "service_advisor_name", length = 100, nullable = false)
    private String serviceAdvisorName;
    @Column(name = "technician_name", length = 100, nullable = false)
    private String technicianName;
    @Column(name = "fault_description", length = 1000, nullable = false)
    private String faultDescription;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private WorkOrderStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "completed_at")
    private Instant completedAt;

    protected WorkOrder() {
    }

    public WorkOrder(String id, String vehicleId, String partId, int partQuantity,
                     String serviceAdvisorName, String technicianName, String faultDescription) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.partId = partId;
        this.partQuantity = partQuantity;
        this.serviceAdvisorName = serviceAdvisorName;
        this.technicianName = technicianName;
        this.faultDescription = faultDescription;
        this.status = WorkOrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public void complete() {
        this.status = WorkOrderStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getVehicleId() { return vehicleId; }
    public String getPartId() { return partId; }
    public int getPartQuantity() { return partQuantity; }
    public String getServiceAdvisorName() { return serviceAdvisorName; }
    public String getTechnicianName() { return technicianName; }
    public String getFaultDescription() { return faultDescription; }
    public WorkOrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}
