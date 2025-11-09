package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.RoutesApi;
import com.necpgame.backjava.model.GetRoutes200Response;
import com.necpgame.backjava.model.RouteDetailed;
import com.necpgame.backjava.service.RoutesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutesController implements RoutesApi {

    private final RoutesService routesService;

    public RoutesController(RoutesService routesService) {
        this.routesService = routesService;
    }

    @Override
    public ResponseEntity<RouteDetailed> getRoute(String routeId) {
        return ResponseEntity.ok(routesService.getRoute(routeId));
    }

    @Override
    public ResponseEntity<GetRoutes200Response> getRoutes(String origin, String destination, String vehicleType) {
        return ResponseEntity.ok(routesService.getRoutes(origin, destination, vehicleType));
    }
}
