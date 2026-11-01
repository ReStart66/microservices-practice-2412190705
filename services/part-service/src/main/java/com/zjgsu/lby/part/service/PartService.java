package com.zjgsu.lby.part.service;

import com.zjgsu.lby.part.dto.PartCreateRequest;
import com.zjgsu.lby.part.exception.BusinessRuleException;
import com.zjgsu.lby.part.exception.ResourceNotFoundException;
import com.zjgsu.lby.part.model.Part;
import com.zjgsu.lby.part.repository.PartRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartService {
    private final PartRepository parts;

    public PartService(PartRepository parts) {
        this.parts = parts;
    }

    @Transactional
    public Part create(PartCreateRequest request) {
        String code = request.code().trim().toUpperCase(Locale.ROOT);
        if (parts.existsByCode(code)) {
            throw new BusinessRuleException("配件编码已存在: " + code);
        }
        return parts.save(new Part(UUID.randomUUID().toString(), code,
            request.name().trim(), request.stockQuantity()));
    }

    @Transactional(readOnly = true)
    public List<Part> findAll() {
        return parts.findAll();
    }

    @Transactional(readOnly = true)
    public Part findById(String id) {
        return parts.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("配件不存在: " + id));
    }

    @Transactional
    public Part reserve(String id, int quantity) {
        Part part = findById(id);
        try {
            part.reserve(quantity);
        } catch (IllegalStateException exception) {
            throw new BusinessRuleException(exception.getMessage());
        }
        return parts.save(part);
    }
}
