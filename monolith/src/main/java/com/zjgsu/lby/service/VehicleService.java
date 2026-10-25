package com.zjgsu.lby.service;

import com.zjgsu.lby.dto.VehicleCreateRequest;
import com.zjgsu.lby.exception.BusinessRuleException;
import com.zjgsu.lby.exception.ResourceNotFoundException;
import com.zjgsu.lby.model.Vehicle;
import com.zjgsu.lby.repository.VehicleRepository;
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
        String plateNumber = normalizePlateNumber(request.plateNumber());
        if (vehicles.existsByPlateNumber(plateNumber)) {
            throw new BusinessRuleException("车牌号已存在: " + plateNumber);
        }

        Vehicle vehicle = new Vehicle(
            UUID.randomUUID().toString(),
            request.ownerName().trim(),
            plateNumber,
            request.brand().trim(),
            request.model().trim(),
            request.mileage()
        );
        return vehicles.save(vehicle);
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

    private String normalizePlateNumber(String plateNumber) {
        return plateNumber.trim().replace(" ", "").toUpperCase(Locale.ROOT);
    }
}
