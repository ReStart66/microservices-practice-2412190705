package com.zjgsu.lby.vehicle.service;

import com.zjgsu.lby.vehicle.dto.VehicleCreateRequest;
import com.zjgsu.lby.vehicle.exception.BusinessRuleException;
import com.zjgsu.lby.vehicle.exception.ResourceNotFoundException;
import com.zjgsu.lby.vehicle.model.Vehicle;
import com.zjgsu.lby.vehicle.repository.VehicleRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {
    private final VehicleRepository vehicles;

    public VehicleService(VehicleRepository vehicles) {
        this.vehicles = vehicles;
    }

    @Transactional
    public Vehicle create(VehicleCreateRequest request) {
        String plate = normalize(request.plateNumber());
        if (vehicles.existsByPlateNumber(plate)) {
            throw new BusinessRuleException("车牌号已存在: " + plate);
        }
        return vehicles.save(new Vehicle(UUID.randomUUID().toString(), request.ownerName().trim(),
            plate, request.brand().trim(), request.model().trim(), request.mileage()));
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicles.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(String id) {
        return vehicles.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("车辆不存在: " + id));
    }

    private String normalize(String plate) {
        return plate.trim().replace(" ", "").toUpperCase(Locale.ROOT);
    }
}
