package com.emsi.conge_service.entity;


import com.emsi.conge_service.enums.Statut;
import com.emsi.conge_service.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int nombreDeJours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type typeConge;

    @Column(nullable = false)
    private LocalDate dateDebut;
    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(nullable = false)
    private LocalDate dateDemande = LocalDate.now();

    @Column(nullable = true)
    private String motif;

    @Column(nullable = false)
    private Long employeId;
}
