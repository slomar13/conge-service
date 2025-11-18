package com.emsi.conge_service.dto;

import com.emsi.conge_service.enums.Statut;
import com.emsi.conge_service.enums.Type;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CongeResponse {

    private Long id;
    private int nombreDeJours;
    private Type type;
    private Statut statut;
    private LocalDate dateDemande;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;

}
