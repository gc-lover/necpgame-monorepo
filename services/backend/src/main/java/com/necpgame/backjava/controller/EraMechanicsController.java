package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.EraMechanicsApi;
import com.necpgame.backjava.model.DCScaling;
import com.necpgame.backjava.model.EraInfo;
import com.necpgame.backjava.model.EraMechanics;
import com.necpgame.backjava.model.GetFactionAISliders200Response;
import com.necpgame.backjava.model.GetTechnologyAccess200Response;
import com.necpgame.backjava.service.EraMechanicsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EraMechanicsController implements EraMechanicsApi {

    private final EraMechanicsService eraMechanicsService;

    public EraMechanicsController(EraMechanicsService eraMechanicsService) {
        this.eraMechanicsService = eraMechanicsService;
    }

    @Override
    public ResponseEntity<EraInfo> getCurrentEra() {
        return ResponseEntity.ok(eraMechanicsService.getCurrentEra());
    }

    @Override
    public ResponseEntity<DCScaling> getDCScaling(String era) {
        return ResponseEntity.ok(eraMechanicsService.getDCScaling(era));
    }

    @Override
    public ResponseEntity<EraMechanics> getEraMechanics(String era) {
        return ResponseEntity.ok(eraMechanicsService.getEraMechanics(era));
    }

    @Override
    public ResponseEntity<GetFactionAISliders200Response> getFactionAISliders() {
        return ResponseEntity.ok(eraMechanicsService.getFactionAISliders());
    }

    @Override
    public ResponseEntity<GetTechnologyAccess200Response> getTechnologyAccess() {
        return ResponseEntity.ok(eraMechanicsService.getTechnologyAccess());
    }
}

