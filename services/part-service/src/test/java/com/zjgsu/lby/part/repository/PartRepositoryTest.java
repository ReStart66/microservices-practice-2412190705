package com.zjgsu.lby.part.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zjgsu.lby.part.model.Part;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class PartRepositoryTest {
    @Autowired PartRepository parts;

    @Test
    void savesAndFindsByCode() {
        parts.saveAndFlush(new Part("p1", "P001", "Oil Filter", 10));
        assertEquals("Oil Filter", parts.findByCode("P001").orElseThrow().getName());
    }

    @Test
    void enforcesUniqueCode() {
        parts.saveAndFlush(new Part("p1", "P001", "Oil Filter", 10));
        assertThrows(DataIntegrityViolationException.class,
            () -> parts.saveAndFlush(new Part("p2", "P001", "Other", 5)));
    }
}
