package com.zjgsu.lby.part.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PartCreateRequest(
    @NotBlank @Size(max = 32) String code,
    @NotBlank @Size(max = 100) String name,
    @NotNull @Min(0) Integer stockQuantity
) {
}
