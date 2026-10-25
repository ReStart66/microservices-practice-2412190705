package com.zjgsu.lby.controller;

import com.zjgsu.lby.dto.WorkOrderCreateRequest;
import com.zjgsu.lby.dto.WorkOrderResponse;
import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.service.WorkOrderService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrders;

    public WorkOrderController(WorkOrderService workOrders) {
        this.workOrders = workOrders;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> create(@Valid @RequestBody WorkOrderCreateRequest request) {
        WorkOrder workOrder = workOrders.create(request);
        return ResponseEntity
            .created(URI.create("/api/work-orders/" + workOrder.getId()))
            .body(WorkOrderResponse.from(workOrder));
    }

    @GetMapping
    public List<WorkOrderResponse> findAll(@RequestParam(required = false) String vehicleId) {
        return workOrders.findAll(vehicleId).stream().map(WorkOrderResponse::from).toList();
    }

    @GetMapping("/{id}")
    public WorkOrderResponse findById(@PathVariable String id) {
        return WorkOrderResponse.from(workOrders.findById(id));
    }

    @PatchMapping("/{id}/complete")
    public WorkOrderResponse complete(@PathVariable String id) {
        return WorkOrderResponse.from(workOrders.complete(id));
    }
}
