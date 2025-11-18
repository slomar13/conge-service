package com.emsi.conge_service.repository;

import com.emsi.conge_service.entity.Conge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByEmployeId(Long employeId);

    boolean existsByEmployeId(Long employeId);
}
