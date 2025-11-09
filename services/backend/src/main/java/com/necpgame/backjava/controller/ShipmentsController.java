package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.ShipmentsApi;
import com.necpgame.backjava.model.Convoy;
import com.necpgame.backjava.model.CreateConvoyRequest;
import com.necpgame.backjava.model.CreateShipmentRequest;
import com.necpgame.backjava.model.GetCharacterShipments200Response;
import com.necpgame.backjava.model.RequestEscortRequest;
import com.necpgame.backjava.model.Shipment;
import com.necpgame.backjava.model.ShipmentDetailed;
import com.necpgame.backjava.service.ShipmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ShipmentsController implements ShipmentsApi {

    private final ShipmentsService shipmentsService;

    public ShipmentsController(ShipmentsService shipmentsService) {
        this.shipmentsService = shipmentsService;
    }

    @Override
    public ResponseEntity<Convoy> createConvoy(CreateConvoyRequest createConvoyRequest) {
        Convoy convoy = shipmentsService.createConvoy(createConvoyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(convoy);
    }

    @Override
    public ResponseEntity<Shipment> createShipment(CreateShipmentRequest createShipmentRequest) {
        Shipment shipment = shipmentsService.createShipment(createShipmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @Override
    public ResponseEntity<GetCharacterShipments200Response> getCharacterShipments(UUID characterId, String status) {
        return ResponseEntity.ok(shipmentsService.getCharacterShipments(characterId, status));
    }

    @Override
    public ResponseEntity<ShipmentDetailed> getShipment(UUID shipmentId) {
        return ResponseEntity.ok(shipmentsService.getShipment(shipmentId));
    }

    @Override
    public ResponseEntity<Void> requestEscort(RequestEscortRequest requestEscortRequest) {
        shipmentsService.requestEscort(requestEscortRequest);
        return ResponseEntity.ok().build();
    }
}
