package com.emsi.conge_service.client;

import com.emsi.conge_service.dto.EmployeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employe-service")
@Component
public interface EmployeClient {
    @GetMapping(value = "/{id}")
    EmployeResponse getEmploye(@PathVariable(name = "id") Long id);

    @PatchMapping("/solde/{id}/{days}")
    void decrementSoldeConges(@PathVariable Long id, @PathVariable int days);
}
