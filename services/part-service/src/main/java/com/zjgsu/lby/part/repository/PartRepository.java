package com.zjgsu.lby.part.repository;

import com.zjgsu.lby.part.model.Part;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, String> {
    Optional<Part> findByCode(String code);
    boolean existsByCode(String code);
}
