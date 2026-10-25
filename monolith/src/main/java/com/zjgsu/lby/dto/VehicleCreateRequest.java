package com.zjgsu.lby.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleCreateRequest(
    @NotBlank @Size(max = 100) String ownerName,
    @NotBlank @Size(max = 32) String plateNumber,
    @NotBlank @Size(max = 64) String brand,
    @NotBlank @Size(max = 64) String model,
    @NotNull @Min(0) Integer mileage
) {
}
