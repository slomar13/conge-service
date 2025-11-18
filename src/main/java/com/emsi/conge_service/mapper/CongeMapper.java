package com.emsi.conge_service.mapper;

import com.emsi.conge_service.dto.CongeRequest;
import com.emsi.conge_service.dto.CongeResponse;
import com.emsi.conge_service.entity.Conge;

public class CongeMapper {

    private CongeMapper(){}

    public static Conge toEntity(CongeRequest request){
        if (request == null) return null;

        Conge conge = new Conge();
        conge.setTypeConge(request.getTypeConge());
        conge.setDateDebut(request.getDateDebut());
        conge.setDateFin(request.getDateFin());
        conge.setMotif(request.getMotif().isEmpty() ? null : request.getMotif());
        conge.setEmployeId(request.getEmployeId());

        return conge;
    }

    public static CongeResponse toResponse(Conge conge){
        if (conge == null) return null;

        CongeResponse congeResponse = new CongeResponse();
        congeResponse.setId(conge.getId());
        congeResponse.setNombreDeJours(conge.getNombreDeJours());
        congeResponse.setDateDemande(conge.getDateDemande());
        congeResponse.setDateDebut(conge.getDateDebut());
        congeResponse.setDateFin(conge.getDateFin());
        congeResponse.setMotif(conge.getMotif());
        congeResponse.setType(conge.getTypeConge());
        congeResponse.setStatut(conge.getStatut());
        return congeResponse;


    }


}
