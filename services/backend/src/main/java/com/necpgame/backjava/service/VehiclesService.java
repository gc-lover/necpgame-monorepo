package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetVehicles200Response;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for VehiclesService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface VehiclesService {

    /**
     * GET /gameplay/economy/logistics/vehicles : Получить доступные транспортные средства
     *
     * @return GetVehicles200Response
     */
    GetVehicles200Response getVehicles();
}

