package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.InsuranceApi;
import com.necpgame.backjava.model.GetInsurancePlans200Response;
import com.necpgame.backjava.model.Insurance;
import com.necpgame.backjava.model.InsuranceRequest;
import com.necpgame.backjava.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsuranceController implements InsuranceApi {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Override
    public ResponseEntity<GetInsurancePlans200Response> getInsurancePlans() {
        return ResponseEntity.ok(insuranceService.getInsurancePlans());
    }

    @Override
    public ResponseEntity<Insurance> purchaseInsurance(InsuranceRequest insuranceRequest) {
        return ResponseEntity.ok(insuranceService.purchaseInsurance(insuranceRequest));
    }
}
