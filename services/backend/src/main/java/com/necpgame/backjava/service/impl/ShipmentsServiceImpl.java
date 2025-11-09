package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LogisticsCargoItemEntity;
import com.necpgame.backjava.entity.LogisticsConvoyEntity;
import com.necpgame.backjava.entity.LogisticsConvoyMemberEntity;
import com.necpgame.backjava.entity.LogisticsConvoyShipmentEntity;
import com.necpgame.backjava.entity.LogisticsEscortRequestEntity;
import com.necpgame.backjava.entity.LogisticsIncidentEntity;
import com.necpgame.backjava.entity.LogisticsInsuranceEntity;
import com.necpgame.backjava.entity.LogisticsInsurancePlanEntity;
import com.necpgame.backjava.entity.LogisticsRouteEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import com.necpgame.backjava.entity.LogisticsTrackingEventEntity;
import com.necpgame.backjava.entity.enums.LogisticsConvoyStatus;
import com.necpgame.backjava.entity.enums.LogisticsEscortType;
import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import com.necpgame.backjava.entity.enums.LogisticsShipmentPriority;
import com.necpgame.backjava.entity.enums.LogisticsShipmentStatus;
import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import com.necpgame.backjava.model.Convoy;
import com.necpgame.backjava.model.CreateConvoyRequest;
import com.necpgame.backjava.model.CreateShipmentRequest;
import com.necpgame.backjava.model.GetCharacterShipments200Response;
import com.necpgame.backjava.model.RequestEscortRequest;
import com.necpgame.backjava.model.Shipment;
import com.necpgame.backjava.model.ShipmentDetailed;
import com.necpgame.backjava.repository.LogisticsCargoItemRepository;
import com.necpgame.backjava.repository.LogisticsConvoyMemberRepository;
import com.necpgame.backjava.repository.LogisticsConvoyRepository;
import com.necpgame.backjava.repository.LogisticsConvoyShipmentRepository;
import com.necpgame.backjava.repository.LogisticsEscortRequestRepository;
import com.necpgame.backjava.repository.LogisticsIncidentRepository;
import com.necpgame.backjava.repository.LogisticsInsurancePlanRepository;
import com.necpgame.backjava.repository.LogisticsInsuranceRepository;
import com.necpgame.backjava.repository.LogisticsRouteRepository;
import com.necpgame.backjava.repository.LogisticsShipmentRepository;
import com.necpgame.backjava.repository.LogisticsTrackingEventRepository;
import com.necpgame.backjava.service.ShipmentsService;
import com.necpgame.backjava.service.mapper.LogisticsMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShipmentsServiceImpl implements ShipmentsService {

    private static final int DEFAULT_ESTIMATION_HOURS = 4;
    private static final BigDecimal MAX_RISK_REDUCTION = new BigDecimal("0.90");
    private static final BigDecimal BASE_RISK_REDUCTION = new BigDecimal("0.10");
    private static final BigDecimal STEP_RISK_REDUCTION = new BigDecimal("0.10");

    private final LogisticsShipmentRepository shipmentRepository;
    private final LogisticsCargoItemRepository cargoItemRepository;
    private final LogisticsRouteRepository routeRepository;
    private final LogisticsInsuranceRepository insuranceRepository;
    private final LogisticsInsurancePlanRepository insurancePlanRepository;
    private final LogisticsIncidentRepository incidentRepository;
    private final LogisticsTrackingEventRepository trackingEventRepository;
    private final LogisticsConvoyRepository convoyRepository;
    private final LogisticsConvoyMemberRepository convoyMemberRepository;
    private final LogisticsConvoyShipmentRepository convoyShipmentRepository;
    private final LogisticsEscortRequestRepository escortRequestRepository;
    private final LogisticsMapper mapper;

    public ShipmentsServiceImpl(LogisticsShipmentRepository shipmentRepository,
                                LogisticsCargoItemRepository cargoItemRepository,
                                LogisticsRouteRepository routeRepository,
                                LogisticsInsuranceRepository insuranceRepository,
                                LogisticsInsurancePlanRepository insurancePlanRepository,
                                LogisticsIncidentRepository incidentRepository,
                                LogisticsTrackingEventRepository trackingEventRepository,
                                LogisticsConvoyRepository convoyRepository,
                                LogisticsConvoyMemberRepository convoyMemberRepository,
                                LogisticsConvoyShipmentRepository convoyShipmentRepository,
                                LogisticsEscortRequestRepository escortRequestRepository,
                                LogisticsMapper mapper) {
        this.shipmentRepository = shipmentRepository;
        this.cargoItemRepository = cargoItemRepository;
        this.routeRepository = routeRepository;
        this.insuranceRepository = insuranceRepository;
        this.insurancePlanRepository = insurancePlanRepository;
        this.incidentRepository = incidentRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.convoyRepository = convoyRepository;
        this.convoyMemberRepository = convoyMemberRepository;
        this.convoyShipmentRepository = convoyShipmentRepository;
        this.escortRequestRepository = escortRequestRepository;
        this.mapper = mapper;
    }

    @Override
    public Shipment createShipment(CreateShipmentRequest request) {
        UUID characterId = requireUuid(request.getCharacterId(), "character_id");
        LogisticsVehicleType vehicleType = parseEnum(request.getVehicleType() != null ? request.getVehicleType().getValue() : null, LogisticsVehicleType.class, "vehicle_type");
        LogisticsShipmentPriority priority = parseEnumOrDefault(request.getPriority() != null ? request.getPriority().getValue() : null, LogisticsShipmentPriority.class, LogisticsShipmentPriority.NORMAL);
        LogisticsInsurancePlan insurancePlan = parseEnumOrDefault(request.getInsurancePlan() != null ? request.getInsurancePlan().getValue() : null, LogisticsInsurancePlan.class, LogisticsInsurancePlan.NONE);

        LogisticsRouteEntity route = null;
        String routeIdValue = request.getRouteId() != null && request.getRouteId().isPresent() ? request.getRouteId().get() : null;
        if (routeIdValue != null && !routeIdValue.isBlank()) {
            route = routeRepository.findById(UUID.fromString(routeIdValue))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
        }

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime estimatedDelivery = calculateEstimatedDelivery(now, route);

        LogisticsShipmentEntity shipment = LogisticsShipmentEntity.builder()
                .id(UUID.randomUUID())
                .characterId(characterId)
                .status(LogisticsShipmentStatus.PENDING)
                .origin(requireText(request.getOrigin(), "origin"))
                .destination(requireText(request.getDestination(), "destination"))
                .vehicleType(vehicleType)
                .priority(priority)
                .insurancePlan(insurancePlan)
                .route(route)
                .estimatedDelivery(estimatedDelivery)
                .createdAt(now)
                .escortRequested(false)
                .build();

        shipment = shipmentRepository.save(shipment);

        List<LogisticsCargoItemEntity> cargoEntities = createCargoItems(shipment, request);
        if (!cargoEntities.isEmpty()) {
            cargoItemRepository.saveAll(cargoEntities);
        }

        createInsuranceIfNeeded(shipment, insurancePlan, request, cargoEntities);

        return mapper.toShipment(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentDetailed getShipment(UUID shipmentId) {
        LogisticsShipmentEntity shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        List<LogisticsCargoItemEntity> cargoItems = cargoItemRepository.findByShipment(shipment);
        LogisticsRouteEntity route = shipment.getRoute();
        LogisticsInsuranceEntity insurance = insuranceRepository.findByShipment(shipment).orElse(null);
        List<LogisticsIncidentEntity> incidents = incidentRepository.findByShipment(shipment);
        List<LogisticsTrackingEventEntity> trackingEvents = trackingEventRepository.findByShipmentOrderByOccurredAtAsc(shipment);

        return mapper.toShipmentDetailed(shipment, cargoItems, route, insurance, incidents, trackingEvents);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCharacterShipments200Response getCharacterShipments(UUID characterId, String status) {
        List<LogisticsShipmentEntity> entities;
        if (status == null || status.isBlank()) {
            entities = shipmentRepository.findByCharacterId(characterId);
        } else {
            LogisticsShipmentStatus shipmentStatus = parseEnum(status, LogisticsShipmentStatus.class, "status");
            entities = shipmentRepository.findByCharacterIdAndStatus(characterId, shipmentStatus);
        }

        List<Shipment> shipments = entities.stream()
                .map(mapper::toShipment)
                .collect(Collectors.toList());
        return mapper.toCharacterShipmentsResponse(shipments);
    }

    @Override
    public Convoy createConvoy(CreateConvoyRequest createConvoyRequest) {
        UUID leaderId = requireUuid(createConvoyRequest.getLeaderCharacterId(), "leader_character_id");
        List<UUID> shipmentIds = createConvoyRequest.getShipments() != null
                ? createConvoyRequest.getShipments()
                : List.of();

        List<LogisticsShipmentEntity> shipments = shipmentIds.stream()
                .map(id -> shipmentRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment %s not found".formatted(id))))
                .collect(Collectors.toList());

        LogisticsConvoyEntity convoy = LogisticsConvoyEntity.builder()
                .id(UUID.randomUUID())
                .leaderId(leaderId)
                .status(LogisticsConvoyStatus.ACTIVE)
                .riskReduction(calculateRiskReduction(shipments.size()))
                .createdAt(OffsetDateTime.now())
                .build();
        LogisticsConvoyEntity persistedConvoy = convoyRepository.save(convoy);

        List<LogisticsConvoyMemberEntity> members = new ArrayList<>();
        members.add(LogisticsConvoyMemberEntity.builder()
                .id(UUID.randomUUID())
                .convoy(persistedConvoy)
                .characterId(leaderId)
                .build());
        convoyMemberRepository.saveAll(members);

        List<LogisticsConvoyShipmentEntity> convoyShipments = shipments.stream()
                .map(shipment -> LogisticsConvoyShipmentEntity.builder()
                        .id(UUID.randomUUID())
                        .convoy(persistedConvoy)
                        .shipment(shipment)
                        .build())
                .collect(Collectors.toList());
        convoyShipmentRepository.saveAll(convoyShipments);

        return mapper.toConvoy(persistedConvoy, members, convoyShipments);
    }

    @Override
    public Void requestEscort(RequestEscortRequest requestEscortRequest) {
        UUID shipmentId = requireUuid(requestEscortRequest.getShipmentId(), "shipment_id");
        LogisticsEscortType escortType = parseEnum(requestEscortRequest.getEscortType() != null ? requestEscortRequest.getEscortType().getValue() : null, LogisticsEscortType.class, "escort_type");

        LogisticsShipmentEntity shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        LogisticsEscortRequestEntity escort = LogisticsEscortRequestEntity.builder()
                .id(UUID.randomUUID())
                .shipment(shipment)
                .escortType(escortType)
                .payment(requestEscortRequest.getPayment())
                .requestedAt(OffsetDateTime.now())
                .build();
        escortRequestRepository.save(escort);

        shipment.setEscortRequested(true);
        shipmentRepository.save(shipment);
        return null;
    }

    private List<LogisticsCargoItemEntity> createCargoItems(LogisticsShipmentEntity shipment, CreateShipmentRequest request) {
        if (request.getCargo() == null) {
            return List.of();
        }
        return request.getCargo().stream()
                .map(item -> LogisticsCargoItemEntity.builder()
                        .id(UUID.randomUUID())
                        .shipment(shipment)
                        .itemId(item.getItemId() != null ? item.getItemId().toString() : UUID.randomUUID().toString())
                        .quantity(item.getQuantity() != null ? item.getQuantity() : 0)
                        .weight(item.getWeight() != null ? BigDecimal.valueOf(item.getWeight()) : null)
                        .volume(item.getVolume() != null ? BigDecimal.valueOf(item.getVolume()) : null)
                        .value(item.getValue() != null ? BigDecimal.valueOf(item.getValue()) : null)
                        .fragile(Boolean.TRUE.equals(item.getFragile()))
                        .build())
                .collect(Collectors.toList());
    }

    private void createInsuranceIfNeeded(LogisticsShipmentEntity shipment,
                                         LogisticsInsurancePlan plan,
                                         CreateShipmentRequest request,
                                         List<LogisticsCargoItemEntity> cargoItems) {
        if (plan == null || plan == LogisticsInsurancePlan.NONE) {
            return;
        }

        LogisticsInsurancePlanEntity planEntity = insurancePlanRepository.findById(plan)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insurance plan not available"));

        int totalValue = cargoItems.stream()
                .map(LogisticsCargoItemEntity::getValue)
                .filter(value -> value != null)
                .map(BigDecimal::intValue)
                .reduce(0, Integer::sum);

        BigDecimal costPercentage = planEntity.getCostPercentage() != null ? planEntity.getCostPercentage() : BigDecimal.ZERO;
        int cost = costPercentage.multiply(BigDecimal.valueOf(totalValue)).setScale(0, RoundingMode.HALF_UP).intValue();

        LogisticsInsuranceEntity insurance = LogisticsInsuranceEntity.builder()
                .id(UUID.randomUUID())
                .shipment(shipment)
                .plan(plan)
                .coveragePercentage(planEntity.getCoveragePercentage())
                .maxCoverage(planEntity.getMaxCoverage())
                .cost(cost)
                .purchasedAt(OffsetDateTime.now())
                .totalCargoValue(BigDecimal.valueOf(totalValue))
                .build();

        insuranceRepository.save(insurance);
        shipment.setInsuranceCost(BigDecimal.valueOf(cost));
        shipmentRepository.save(shipment);
    }

    private OffsetDateTime calculateEstimatedDelivery(OffsetDateTime createdAt, LogisticsRouteEntity route) {
        if (route == null || route.getEstimatedTimeHours() == null) {
            return createdAt.plusHours(DEFAULT_ESTIMATION_HOURS);
        }
        double hours = route.getEstimatedTimeHours();
        long seconds = (long) Math.round(hours * 3600);
        return createdAt.plusSeconds(seconds);
    }

    private BigDecimal calculateRiskReduction(int shipmentCount) {
        if (shipmentCount <= 0) {
            return BASE_RISK_REDUCTION;
        }
        BigDecimal increment = STEP_RISK_REDUCTION.multiply(BigDecimal.valueOf(shipmentCount));
        BigDecimal result = BASE_RISK_REDUCTION.add(increment);
        return result.min(MAX_RISK_REDUCTION);
    }

    private UUID requireUuid(UUID value, String field) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing " + field);
        }
        return value;
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing " + field);
        }
        return value;
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> type, String field) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing " + field);
        }
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + field);
        }
    }

    private <E extends Enum<E>> E parseEnumOrDefault(String value, Class<E> type, E fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for " + type.getSimpleName());
        }
    }
}
