package com.emsi.conge_service.controller;

import com.emsi.conge_service.service.CongeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CongeController {
    private final CongeService congeService;


}
