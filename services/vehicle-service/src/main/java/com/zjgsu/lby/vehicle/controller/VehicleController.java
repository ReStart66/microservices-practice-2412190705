package com.zjgsu.lby.vehicle.controller;

import com.zjgsu.lby.vehicle.dto.VehicleCreateRequest;
import com.zjgsu.lby.vehicle.dto.VehicleResponse;
import com.zjgsu.lby.vehicle.model.Vehicle;
import com.zjgsu.lby.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicles;

    public VehicleController(VehicleService vehicles) {
        this.vehicles = vehicles;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleCreateRequest request) {
        Vehicle vehicle = vehicles.create(request);
        return ResponseEntity.created(URI.create("/api/vehicles/" + vehicle.getId()))
            .body(VehicleResponse.from(vehicle));
    }

    @GetMapping
    public List<VehicleResponse> findAll() {
        return vehicles.findAll().stream().map(VehicleResponse::from).toList();
    }

    @GetMapping("/{id}")
    public VehicleResponse findById(@PathVariable String id) {
        return VehicleResponse.from(vehicles.findById(id));
    }
}
