package com.emsi.conge_service.service;

import com.emsi.conge_service.dto.CongeRequest;
import com.emsi.conge_service.entity.Conge;
import com.emsi.conge_service.enums.Statut;
import com.emsi.conge_service.exception.CongeNotFoundException;
import com.emsi.conge_service.mapper.CongeMapper;
import com.emsi.conge_service.dto.CongeResponse;
import com.emsi.conge_service.repository.CongeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CongeService {

    private final CongeRepository congeRepository;

    public Conge getConge(Long id){
        Conge conge = congeRepository.findById(id).orElseThrow(() -> new CongeNotFoundException("Congé " + id + " introuvable"));
        return conge;
    }

    public List<CongeResponse> getCongeList(){
        List<CongeResponse> congeList = congeRepository.findAll().stream().map(CongeMapper::toResponse).collect(Collectors.toList());
        return congeList;
    }

    public List<CongeResponse> getCongesByEmployeId(Long employeId){
        // à verifier si l'employe existe via employe-service

        List<CongeResponse> congeList = congeRepository.findByEmployeId(employeId).stream().map(CongeMapper::toResponse).collect(Collectors.toList());
        return congeList;
    }

    @Transactional
    public Conge addConge(CongeRequest request){
        if (!isDateDebutInferieureDateFin(request.getDateDebut(), request.getDateFin())){
            throw new RuntimeException("La date début doit être inferieure à la date fin ");
        }
        if (isDateDebutSuperieureAujourdhui(request.getDateDebut())){
            throw new RuntimeException("La date début doit être supérieur à celle d'aujourd'hui");
        }
        Conge conge = CongeMapper.toEntity(request);
        conge.setNombreDeJours(calculerNombreDeJours(request.getDateDebut(), request.getDateFin()));
        return congeRepository.save(conge);
    }

    @Transactional
    public void deleteConge(Long id){
        Conge conge = congeRepository.findById(id).orElseThrow(() -> new CongeNotFoundException("Congé " + id + " introuvable"));
        if (conge.getStatut() != Statut.EN_ATTENTE){
            throw new RuntimeException("Seuls les congés en statut \"en attente\" sont supprimables.");
        }
         congeRepository.delete(conge);
    }

    @Transactional
    public Conge updateConge(Long id, CongeRequest request){
        Conge conge = congeRepository.findById(id).orElseThrow(() -> new CongeNotFoundException("Congé " + id + " introuvable"));

        if (!isDateDebutInferieureDateFin(request.getDateDebut(), request.getDateFin())){
            throw new RuntimeException("La date début doit être inferieure à la date fin ");
        }
        if (isDateDebutSuperieureAujourdhui(request.getDateDebut())){
            throw new RuntimeException("La date début doit être supérieur à celle d'aujourd'hui");
        }

        if (conge.getStatut() != Statut.EN_ATTENTE) throw new RuntimeException("Seuls les congés en statut \"en attente\" sont modifiables.");

        conge.setNombreDeJours(calculerNombreDeJours(request.getDateDebut(), request.getDateFin()));
        conge.setTypeConge(request.getTypeConge());
        conge.setTypeConge(request.getTypeConge());
        conge.setMotif(request.getMotif());
        conge.setDateDebut(request.getDateDebut());
        conge.setDateFin(request.getDateFin());
        conge.setDateDemande(LocalDate.now());

        return congeRepository.save(conge);
    }


    //utilitaires
    private int calculerNombreDeJours(LocalDate dateDebut, LocalDate dateFin) {
        return (int) ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
    }

    private boolean isDateDebutInferieureDateFin(LocalDate dateDebut, LocalDate dateFin) {
        return dateDebut.isBefore(dateFin);
    }

    private boolean isDateDebutSuperieureAujourdhui(LocalDate dateDebut) {
        return dateDebut.isAfter(LocalDate.now());
    }


}
