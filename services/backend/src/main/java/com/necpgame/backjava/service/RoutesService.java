package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetRoutes200Response;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.RouteDetailed;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for RoutesService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface RoutesService {

    /**
     * GET /gameplay/economy/logistics/routes/{route_id} : Получить детали маршрута
     *
     * @param routeId  (required)
     * @return RouteDetailed
     */
    RouteDetailed getRoute(String routeId);

    /**
     * GET /gameplay/economy/logistics/routes : Получить доступные маршруты
     *
     * @param origin  (required)
     * @param destination  (required)
     * @param vehicleType  (optional)
     * @return GetRoutes200Response
     */
    GetRoutes200Response getRoutes(String origin, String destination, String vehicleType);
}

