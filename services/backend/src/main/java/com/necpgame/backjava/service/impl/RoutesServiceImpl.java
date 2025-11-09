package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LogisticsRouteEntity;
import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import com.necpgame.backjava.model.GetRoutes200Response;
import com.necpgame.backjava.model.Route;
import com.necpgame.backjava.model.RouteDetailed;
import com.necpgame.backjava.repository.LogisticsRouteRepository;
import com.necpgame.backjava.service.RoutesService;
import com.necpgame.backjava.service.mapper.LogisticsMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoutesServiceImpl implements RoutesService {

    private final LogisticsRouteRepository routeRepository;
    private final LogisticsMapper mapper;

    public RoutesServiceImpl(LogisticsRouteRepository routeRepository, LogisticsMapper mapper) {
        this.routeRepository = routeRepository;
        this.mapper = mapper;
    }

    @Override
    public RouteDetailed getRoute(String routeId) {
        LogisticsRouteEntity route = routeRepository.findById(UUID.fromString(routeId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
        return mapper.toRouteDetailed(route);
    }

    @Override
    public GetRoutes200Response getRoutes(String origin, String destination, String vehicleType) {
        if (origin == null || origin.isBlank() || destination == null || destination.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin and destination are required");
        }

        List<LogisticsRouteEntity> routes = routeRepository.findByOriginIgnoreCaseAndDestinationIgnoreCase(origin, destination);
        if (vehicleType != null && !vehicleType.isBlank()) {
            LogisticsVehicleType type = parseVehicleType(vehicleType);
            routes = routes.stream()
                    .filter(route -> route.getVehicleTypes().stream()
                            .anyMatch(v -> v.getVehicleType() == type))
                    .collect(Collectors.toList());
        }

        List<Route> mapped = routes.stream()
                .map(mapper::toRoute)
                .collect(Collectors.toList());
        return mapper.toRoutesResponse(mapped);
    }

    private LogisticsVehicleType parseVehicleType(String value) {
        try {
            return LogisticsVehicleType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle_type");
        }
    }
}
