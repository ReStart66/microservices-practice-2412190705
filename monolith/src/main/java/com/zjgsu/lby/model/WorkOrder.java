package com.zjgsu.lby.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "service_advisor_name", length = 100, nullable = false)
    private String serviceAdvisorName;

    @Column(name = "technician_name", length = 100, nullable = false)
    private String technicianName;

    @Column(name = "fault_description", length = 1000, nullable = false)
    private String faultDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private WorkOrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    protected WorkOrder() {
    }

    public WorkOrder(String id, Vehicle vehicle, String serviceAdvisorName,
                     String technicianName, String faultDescription) {
        this.id = id;
        this.vehicle = vehicle;
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

    public String getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getServiceAdvisorName() {
        return serviceAdvisorName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
