package com.zjgsu.lby.part.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(@NotNull @Min(1) Integer quantity) {
}
