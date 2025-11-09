package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Convoy;
import com.necpgame.backjava.model.CreateConvoyRequest;
import com.necpgame.backjava.model.CreateShipmentRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GetCharacterShipments200Response;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.RequestEscortRequest;
import com.necpgame.backjava.model.Shipment;
import com.necpgame.backjava.model.ShipmentDetailed;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for ShipmentsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface ShipmentsService {

    /**
     * POST /gameplay/economy/logistics/convoy/create : Создать конвой
     * Совместная доставка с другими игроками
     *
     * @param createConvoyRequest  (required)
     * @return Convoy
     */
    Convoy createConvoy(CreateConvoyRequest createConvoyRequest);

    /**
     * POST /gameplay/economy/logistics/shipments : Создать доставку
     *
     * @param createShipmentRequest  (required)
     * @return Shipment
     */
    Shipment createShipment(CreateShipmentRequest createShipmentRequest);

    /**
     * GET /gameplay/economy/logistics/shipments/character/{character_id} : Получить доставки персонажа
     *
     * @param characterId  (required)
     * @param status  (optional)
     * @return GetCharacterShipments200Response
     */
    GetCharacterShipments200Response getCharacterShipments(UUID characterId, String status);

    /**
     * GET /gameplay/economy/logistics/shipments/{shipment_id} : Получить статус доставки
     *
     * @param shipmentId  (required)
     * @return ShipmentDetailed
     */
    ShipmentDetailed getShipment(UUID shipmentId);

    /**
     * POST /gameplay/economy/logistics/escort/request : Запросить эскорт для доставки
     *
     * @param requestEscortRequest  (required)
     * @return Void
     */
    Void requestEscort(RequestEscortRequest requestEscortRequest);
}

