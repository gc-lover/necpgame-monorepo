package com.necpgame.backjava.service;

import com.necpgame.backjava.model.FactionDetailed;
import com.necpgame.backjava.model.GetFactions200Response;
import com.necpgame.backjava.model.ListFactions200Response;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for FactionsService.
 * Generated from OpenAPI specification.
 *
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface FactionsService {

    /**
     * GET /factions : Legacy factions listing endpoint.
     *
     * @param origin optional origin filter
     * @return GetFactions200Response
     */
    GetFactions200Response getFactions(String origin);

    /**
     * GET /lore/factions : Retrieve lore factions with pagination and optional filters.
     *
     * @param type     optional faction type filter
     * @param region   optional region filter
     * @param page     page number (default 1)
     * @param pageSize page size (default 20)
     * @return ListFactions200Response
     */
    ListFactions200Response listFactions(String type, String region, Integer page, Integer pageSize);

    /**
     * GET /lore/factions/{faction_id} : Retrieve detailed lore faction information.
     *
     * @param factionId external faction identifier
     * @return FactionDetailed
     */
    FactionDetailed getFaction(String factionId);
}

