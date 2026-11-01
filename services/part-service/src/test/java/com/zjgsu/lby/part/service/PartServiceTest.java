package com.zjgsu.lby.part.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.part.dto.PartCreateRequest;
import com.zjgsu.lby.part.exception.BusinessRuleException;
import com.zjgsu.lby.part.model.Part;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PartServiceTest {
    @Autowired PartService parts;

    @Test
    void reservesStockAndRejectsInsufficientQuantity() {
        Part part = parts.create(new PartCreateRequest("P-001", "Oil Filter", 5));
        Part reserved = parts.reserve(part.getId(), 2);
        assertEquals(2, reserved.getReservedQuantity());
        assertEquals(3, reserved.availableQuantity());
        assertThrows(BusinessRuleException.class, () -> parts.reserve(part.getId(), 4));
    }
}
