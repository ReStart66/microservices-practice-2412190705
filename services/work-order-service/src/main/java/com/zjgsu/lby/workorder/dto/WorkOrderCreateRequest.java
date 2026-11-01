package com.zjgsu.lby.workorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WorkOrderCreateRequest(
    @NotBlank String vehicleId,
    @NotBlank String partId,
    @NotNull @Min(1) Integer partQuantity,
    @NotBlank @Size(max = 100) String serviceAdvisorName,
    @NotBlank @Size(max = 100) String technicianName,
    @NotBlank @Size(max = 1000) String faultDescription
) {
}
