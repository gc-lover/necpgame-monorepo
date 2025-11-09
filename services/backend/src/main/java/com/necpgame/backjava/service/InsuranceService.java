package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GetInsurancePlans200Response;
import com.necpgame.backjava.model.Insurance;
import com.necpgame.backjava.model.InsuranceRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for InsuranceService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface InsuranceService {

    /**
     * GET /gameplay/economy/logistics/insurance/plans : Получить доступные планы страхования
     *
     * @return GetInsurancePlans200Response
     */
    GetInsurancePlans200Response getInsurancePlans();

    /**
     * POST /gameplay/economy/logistics/insurance : Купить страховку для груза
     *
     * @param insuranceRequest  (required)
     * @return Insurance
     */
    Insurance purchaseInsurance(InsuranceRequest insuranceRequest);
}

