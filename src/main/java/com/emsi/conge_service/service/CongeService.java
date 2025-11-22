package com.emsi.conge_service.service;

import com.emsi.conge_service.client.EmployeClient;
import com.emsi.conge_service.dto.CongeRequest;
import com.emsi.conge_service.dto.EmployeResponse;
import com.emsi.conge_service.entity.Conge;
import com.emsi.conge_service.enums.Statut;
import com.emsi.conge_service.exception.CongeConflitException;
import com.emsi.conge_service.exception.CongeNotFoundException;
import com.emsi.conge_service.exception.EmployeNotFoundException;
import com.emsi.conge_service.exception.SoldeInsuffisantException;
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
    private final EmployeClient employeClient;

    public CongeResponse getCongeById(Long id){
        Conge conge = congeRepository.findById(id).orElseThrow(() -> new CongeNotFoundException("Congé " + id + " introuvable"));
        return CongeMapper.toResponse(conge);
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
    public CongeResponse addConge(CongeRequest request) {

        // 1. Vérifier que l'employé existe via Feign
        EmployeResponse employe;
        try {
            employe = employeClient.getEmploye(request.getEmployeId());
        } catch (Exception ex) {
            throw new EmployeNotFoundException("Aucun employé trouvé avec l'id : " + request.getEmployeId());
        }

        // 2. Vérifier le solde de congé disponible
        int nombreJoursDemandes = calculerNombreDeJours(request.getDateDebut(), request.getDateFin());
        if (employe.getSoldeConge() < nombreJoursDemandes) {
            throw new SoldeInsuffisantException(
                    "Solde insuffisant : employé (" + request.getEmployeId() + ") n'a que "
                            + employe.getSoldeConge() + " jours restants."
            );
        }

        // 3. Vérifier les conflits de dates (si ta logique le nécessite)
        boolean existeConflit = congeRepository.existsByEmployeIdAndDatesOverlap(
                request.getEmployeId(),
                request.getDateDebut(),
                request.getDateFin()
        );

        if (existeConflit) {
            throw new CongeConflitException("L'employé a déjà un congé sur cette période.");
        }

        // 4. Mapper le CongeRequest vers l'entité Conge
        Conge conge = CongeMapper.toEntity(request);

        // 5. Sauvegarder le congé dans conge-service
        Conge saved = congeRepository.save(conge);

        // 6. Décrémenter le solde dans employe-service via Feign
        try {
            employeClient.decrementSoldeConges(
                    request.getEmployeId(),
                    nombreJoursDemandes
            );
        } catch (Exception ex) {
            // Rollback en cas d'échec côté employe-service
            congeRepository.delete(saved);
            throw new RuntimeException("Erreur lors de la mise à jour du solde de congé.");
        }

        // 7. Retourner la réponse
        return CongeMapper.toResponse(saved);
    }


    @Transactional
    public void deleteConge(Long id){
        Conge conge = congeRepository.findById(id).orElseThrow(() -> new CongeNotFoundException("Congé " + id + " introuvable"));
        if (conge.getStatut() != Statut.EN_ATTENTE){
            throw new RuntimeException("Seuls les congés en statut \"en attente\" sont supprimables.");
        }
         congeRepository.delete(conge);
        // rectifier le solde de congé via employe-service
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
