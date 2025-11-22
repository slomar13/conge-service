package com.emsi.conge_service.repository;

import com.emsi.conge_service.entity.Conge;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByEmployeId(Long employeId);

    boolean existsByEmployeIdAndDatesOverlap(Long employeId,  LocalDate dateDebut,  LocalDate dateFin);
}
