package com.zjgsu.lby.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
    name = "vehicles",
    uniqueConstraints = @UniqueConstraint(name = "uk_vehicles_plate_number", columnNames = "plate_number")
)
public class Vehicle {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "owner_name", length = 100, nullable = false)
    private String ownerName;

    @Column(name = "plate_number", length = 32, nullable = false)
    private String plateNumber;

    @Column(name = "brand", length = 64, nullable = false)
    private String brand;

    @Column(name = "model", length = 64, nullable = false)
    private String model;

    @Column(name = "mileage", nullable = false)
    private int mileage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Vehicle() {
    }

    public Vehicle(String id, String ownerName, String plateNumber, String brand, String model, int mileage) {
        this.id = id;
        this.ownerName = ownerName;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.mileage = mileage;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getMileage() {
        return mileage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
