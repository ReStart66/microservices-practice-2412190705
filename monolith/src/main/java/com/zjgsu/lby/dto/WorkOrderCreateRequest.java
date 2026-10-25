package com.zjgsu.lby.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkOrderCreateRequest(
    @NotBlank String vehicleId,
    @NotBlank @Size(max = 100) String serviceAdvisorName,
    @NotBlank @Size(max = 100) String technicianName,
    @NotBlank @Size(max = 1000) String faultDescription
) {
}
