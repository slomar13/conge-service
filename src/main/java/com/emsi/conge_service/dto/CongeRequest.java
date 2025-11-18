package com.emsi.conge_service.dto;

import com.emsi.conge_service.enums.Type;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CongeRequest {


    @NotNull(message = "Le type de congé est obligatoire")
    private Type typeConge;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @Size(max = 200, message = "Le motif ne doit pas dépasser 200 caractères")
    private String motif;

    @NotNull(message = "L'identifiant de l'employé est obligatoire")
    private Long employeId;
}
