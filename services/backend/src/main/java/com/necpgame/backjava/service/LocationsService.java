package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetConnectedLocations200Response;
import com.necpgame.backjava.model.GetLocationActions200Response;
import com.necpgame.backjava.model.GetLocations200Response;
import com.necpgame.backjava.model.ListLocations200Response;
import com.necpgame.backjava.model.LocationDetailed;
import com.necpgame.backjava.model.LocationDetails;
import com.necpgame.backjava.model.TravelRequest;
import com.necpgame.backjava.model.TravelResponse;
import java.util.UUID;

public interface LocationsService {

    GetLocations200Response getLocations(UUID characterId, String region, String dangerLevel, Integer minLevel);

    LocationDetails getLocationDetails(String locationId, UUID characterId);

    LocationDetails getCurrentLocation(UUID characterId);

    TravelResponse travelToLocation(TravelRequest request);

    GetLocationActions200Response getLocationActions(String locationId, UUID characterId);

    GetConnectedLocations200Response getConnectedLocations(String locationId, UUID characterId);

    ListLocations200Response listLocations(String region, String type, Integer page, Integer pageSize);

    LocationDetailed getLocation(String locationId);
}



