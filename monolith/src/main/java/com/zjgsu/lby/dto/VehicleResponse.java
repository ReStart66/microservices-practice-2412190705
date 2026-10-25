package com.zjgsu.lby.dto;

import com.zjgsu.lby.model.Vehicle;
import java.time.Instant;

public record VehicleResponse(
    String id,
    String ownerName,
    String plateNumber,
    String brand,
    String model,
    int mileage,
    Instant createdAt
) {
    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
            vehicle.getId(),
            vehicle.getOwnerName(),
            vehicle.getPlateNumber(),
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getMileage(),
            vehicle.getCreatedAt()
        );
    }
}
