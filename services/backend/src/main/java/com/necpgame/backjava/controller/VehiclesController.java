package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.VehiclesApi;
import com.necpgame.backjava.model.GetVehicles200Response;
import com.necpgame.backjava.service.VehiclesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehiclesController implements VehiclesApi {

    private final VehiclesService vehiclesService;

    public VehiclesController(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @Override
    public ResponseEntity<GetVehicles200Response> getVehicles() {
        return ResponseEntity.ok(vehiclesService.getVehicles());
    }
}
