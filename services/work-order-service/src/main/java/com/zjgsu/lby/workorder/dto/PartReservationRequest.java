package com.zjgsu.lby.workorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PartReservationRequest(@NotNull @Min(1) Integer quantity) {
}
