package com.emsi.conge_service.dto;

import com.emsi.conge_service.enums.Poste;
import lombok.Data;

@Data
public class EmployeResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Poste poste;
    private String adresse;
    private int soldeConge;
}