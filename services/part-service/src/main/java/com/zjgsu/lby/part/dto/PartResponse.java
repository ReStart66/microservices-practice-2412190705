package com.zjgsu.lby.part.dto;

import com.zjgsu.lby.part.model.Part;
import java.time.Instant;

public record PartResponse(String id, String code, String name, int stockQuantity,
                           int reservedQuantity, int availableQuantity, Instant createdAt) {
    public static PartResponse from(Part part) {
        return new PartResponse(part.getId(), part.getCode(), part.getName(), part.getStockQuantity(),
            part.getReservedQuantity(), part.availableQuantity(), part.getCreatedAt());
    }
}
