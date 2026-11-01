package com.zjgsu.lby.part.controller;

import com.zjgsu.lby.part.dto.PartCreateRequest;
import com.zjgsu.lby.part.dto.PartResponse;
import com.zjgsu.lby.part.dto.ReservationRequest;
import com.zjgsu.lby.part.model.Part;
import com.zjgsu.lby.part.service.PartService;
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
@RequestMapping("/api/parts")
public class PartController {
    private final PartService parts;

    public PartController(PartService parts) {
        this.parts = parts;
    }

    @PostMapping
    public ResponseEntity<PartResponse> create(@Valid @RequestBody PartCreateRequest request) {
        Part part = parts.create(request);
        return ResponseEntity.created(URI.create("/api/parts/" + part.getId()))
            .body(PartResponse.from(part));
    }

    @GetMapping
    public List<PartResponse> findAll() {
        return parts.findAll().stream().map(PartResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PartResponse findById(@PathVariable String id) {
        return PartResponse.from(parts.findById(id));
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<Void> reserve(@PathVariable String id,
                                        @Valid @RequestBody ReservationRequest request) {
        parts.reserve(id, request.quantity());
        return ResponseEntity.noContent().build();
    }
}
