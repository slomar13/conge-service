package com.emsi.conge_service.client;

import com.emsi.conge_service.dto.EmployeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "employe-service", path = "/api/employes")
public interface EmployeClient {
    @GetMapping(value = "/{id}")
    EmployeResponse getEmploye(@PathVariable(name = "id") Long id);

    @PostMapping("/solde/{id}/{days}")
    void decrementSoldeConges(@PathVariable(name = "id") Long id, @PathVariable(name = "days") int days);
}
