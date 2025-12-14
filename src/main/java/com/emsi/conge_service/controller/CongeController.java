package com.emsi.conge_service.controller;


import com.emsi.conge_service.dto.CongeRequest;
import com.emsi.conge_service.dto.CongeResponse;
import com.emsi.conge_service.dto.RejectRequest;
import com.emsi.conge_service.entity.Conge;
import com.emsi.conge_service.service.CongeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/conges")
public class CongeController {
    private final CongeService congeService;

    @GetMapping("/all")
    public List<CongeResponse> conges(){
        return congeService.getCongeList();
    }

    @GetMapping("/{id}")
    public CongeResponse conge(@PathVariable Long id){
        return congeService.getCongeById(id);
    }

    @GetMapping("/employe/{employeId}")
    public List<CongeResponse> employeConge(@PathVariable Long employeId){
        return congeService.getCongesByEmployeId(employeId);
    }

    @PostMapping("/add")
    public CongeResponse addConge(@RequestBody CongeRequest congeRequest){
        System.out.println(congeRequest);
        return congeService.addDemandeConge(congeRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteConge(@PathVariable Long id) {
        congeService.deleteConge(id);
    }

    @PutMapping("/update/{id}")
    public Conge updateConge(@PathVariable Long id, @RequestBody CongeRequest congeRequest) {
        return congeService.updateConge(id, congeRequest);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CongeResponse> approveConge(@PathVariable Long id) {
        return ResponseEntity.ok(congeService.approveConge(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<CongeResponse> rejectConge(
            @PathVariable Long id,
            @RequestBody RejectRequest rejectRequest) {

        return ResponseEntity.ok(congeService.rejectConge(id, rejectRequest.getReason()));
    }

}
