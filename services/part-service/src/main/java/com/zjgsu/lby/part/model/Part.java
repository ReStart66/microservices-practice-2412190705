package com.zjgsu.lby.part.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(name = "parts", uniqueConstraints = @UniqueConstraint(name = "uk_parts_code", columnNames = "code"))
public class Part {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(length = 32, nullable = false)
    private String code;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Part() {
    }

    public Part(String id, String code, String name, int stockQuantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = 0;
        this.createdAt = Instant.now();
    }

    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("预留数量必须大于 0");
        }
        if (availableQuantity() < quantity) {
            throw new IllegalStateException("配件库存不足");
        }
        reservedQuantity += quantity;
    }

    public int availableQuantity() {
        return stockQuantity - reservedQuantity;
    }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getStockQuantity() { return stockQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
    public Instant getCreatedAt() { return createdAt; }
}
