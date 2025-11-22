package com.emsi.conge_service.repository;

import com.emsi.conge_service.entity.Conge;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByEmployeId(Long employeId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Conge c " +
            "WHERE c.employeId = :employeId " +
            "AND (c.dateDebut <= :dateFin AND c.dateFin >= :dateDebut)")
    boolean existsByEmployeIdAndDatesOverlap(
            @Param("employeId") Long employeId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
