package com.veritrabajo.backend.reputation.infrastructure.web;

import com.veritrabajo.backend.reputation.application.ReputationApplicationService;
import com.veritrabajo.backend.reputation.infrastructure.web.dto.TradeReputationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reputations")
@CrossOrigin(origins = "*")
public class ReputationController {

    private final ReputationApplicationService applicationService;

    public ReputationController(ReputationApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<TradeReputationResponse> getByProfileId(@PathVariable String profileId) {
        TradeReputationResponse response = TradeReputationResponse.from(
                applicationService.createIfNotExists(profileId)
        );
        return ResponseEntity.ok(response);
    }
}
