package com.necpgame.backjava.service.mapper;

import com.necpgame.backjava.entity.LogisticsCargoItemEntity;
import com.necpgame.backjava.entity.LogisticsConvoyEntity;
import com.necpgame.backjava.entity.LogisticsConvoyMemberEntity;
import com.necpgame.backjava.entity.LogisticsConvoyShipmentEntity;
import com.necpgame.backjava.entity.LogisticsIncidentCargoLossEntity;
import com.necpgame.backjava.entity.LogisticsIncidentEntity;
import com.necpgame.backjava.entity.LogisticsInsuranceEntity;
import com.necpgame.backjava.entity.LogisticsInsurancePlanEntity;
import com.necpgame.backjava.entity.LogisticsRouteEntity;
import com.necpgame.backjava.entity.LogisticsRouteRiskEntity;
import com.necpgame.backjava.entity.LogisticsRouteWaypointEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import com.necpgame.backjava.entity.LogisticsTrackingEventEntity;
import com.necpgame.backjava.entity.LogisticsVehicleEntity;
import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import com.necpgame.backjava.entity.enums.LogisticsShipmentStatus;
import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import com.necpgame.backjava.model.CargoItem;
import com.necpgame.backjava.model.Convoy;
import com.necpgame.backjava.model.GetCharacterShipments200Response;
import com.necpgame.backjava.model.GetInsurancePlans200Response;
import com.necpgame.backjava.model.GetRoutes200Response;
import com.necpgame.backjava.model.GetVehicles200Response;
import com.necpgame.backjava.model.Incident;
import com.necpgame.backjava.model.IncidentCargoLostInner;
import com.necpgame.backjava.model.Insurance;
import com.necpgame.backjava.model.InsurancePlan;
import com.necpgame.backjava.model.Route;
import com.necpgame.backjava.model.RouteDetailed;
import com.necpgame.backjava.model.RouteRisk;
import com.necpgame.backjava.model.Shipment;
import com.necpgame.backjava.model.ShipmentDetailed;
import com.necpgame.backjava.model.ShipmentDetailedAllOfTrackingHistory;
import com.necpgame.backjava.model.Vehicle;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LogisticsMapper {

    public Shipment toShipment(LogisticsShipmentEntity entity) {
        Shipment shipment = new Shipment();
        fillShipmentBase(entity, shipment);
        return shipment;
    }

    public ShipmentDetailed toShipmentDetailed(LogisticsShipmentEntity shipmentEntity,
                                               List<LogisticsCargoItemEntity> cargoItems,
                                               LogisticsRouteEntity routeEntity,
                                               LogisticsInsuranceEntity insuranceEntity,
                                               List<LogisticsIncidentEntity> incidentEntities,
                                               List<LogisticsTrackingEventEntity> trackingEntities) {
        ShipmentDetailed detailed = new ShipmentDetailed();
        fillShipmentBase(shipmentEntity, detailed);

        detailed.setCargo(cargoItems.stream()
                .map(this::toCargoItem)
                .collect(Collectors.toList()));

        if (routeEntity != null) {
            detailed.setRoute(toRoute(routeEntity));
        }

        if (shipmentEntity.getCurrentLocation() != null) {
            detailed.setCurrentLocation(JsonNullable.of(shipmentEntity.getCurrentLocation()));
        } else {
            detailed.setCurrentLocation(JsonNullable.undefined());
        }

        if (shipmentEntity.getProgressPercentage() != null) {
            detailed.setProgressPercentage(shipmentEntity.getProgressPercentage().floatValue());
        }

        if (insuranceEntity != null) {
            detailed.setInsurance(toInsurance(insuranceEntity));
        }

        detailed.setIncidents(incidentEntities.stream()
                .map(incident -> toIncident(incident, incident.getCargoLosses()))
                .collect(Collectors.toList()));

        detailed.setTrackingHistory(trackingEntities.stream()
                .sorted(Comparator.comparing(LogisticsTrackingEventEntity::getOccurredAt))
                .map(this::toTrackingHistory)
                .collect(Collectors.toList()));

        return detailed;
    }

    public CargoItem toCargoItem(LogisticsCargoItemEntity entity) {
        CargoItem cargoItem = new CargoItem()
                .quantity(entity.getQuantity())
                .weight(entity.getWeight() != null ? entity.getWeight().floatValue() : null)
                .volume(entity.getVolume() != null ? entity.getVolume().floatValue() : null)
                .value(entity.getValue() != null ? entity.getValue().intValue() : null)
                .fragile(entity.isFragile());
        if (isUuid(entity.getItemId())) {
            cargoItem.setItemId(UUID.fromString(entity.getItemId()));
        }
        return cargoItem;
    }

    public Route toRoute(LogisticsRouteEntity entity) {
        Route route = new Route()
                .routeId(entity.getId().toString())
                .name(entity.getName())
                .origin(entity.getOrigin())
                .destination(entity.getDestination())
                .distanceKm(entity.getDistanceKm() != null ? BigDecimal.valueOf(entity.getDistanceKm()) : null)
                .estimatedTimeHours(entity.getEstimatedTimeHours() != null ? BigDecimal.valueOf(entity.getEstimatedTimeHours()) : null)
                .riskLevel(entity.getRiskLevel() != null ? Route.RiskLevelEnum.fromValue(entity.getRiskLevel().name()) : null);

        if (entity.getCostMultiplier() != null) {
            route.setCostMultiplier(entity.getCostMultiplier().floatValue());
        }

        route.setVehicleTypes(entity.getVehicleTypes().stream()
                .map(v -> v.getVehicleType().name())
                .collect(Collectors.toList()));
        return route;
    }

    public RouteDetailed toRouteDetailed(LogisticsRouteEntity entity) {
        Route base = toRoute(entity);
        RouteDetailed detailed = new RouteDetailed()
                .routeId(base.getRouteId())
                .name(base.getName())
                .origin(base.getOrigin())
                .destination(base.getDestination())
                .distanceKm(base.getDistanceKm())
                .estimatedTimeHours(base.getEstimatedTimeHours());
        detailed.setVehicleTypes(base.getVehicleTypes());
        detailed.setCostMultiplier(base.getCostMultiplier());
        if (entity.getRiskLevel() != null) {
            detailed.setRiskLevel(RouteDetailed.RiskLevelEnum.fromValue(entity.getRiskLevel().name()));
        }

        detailed.setWaypoints(entity.getWaypoints().stream()
                .sorted(Comparator.comparing(LogisticsRouteWaypointEntity::getSequenceIndex))
                .map(LogisticsRouteWaypointEntity::getWaypoint)
                .collect(Collectors.toList()));

        detailed.setRisks(entity.getRisks().stream()
                .map(this::toRouteRisk)
                .collect(Collectors.toList()));

        return detailed;
    }

    public RouteRisk toRouteRisk(LogisticsRouteRiskEntity entity) {
        RouteRisk risk = new RouteRisk()
                .riskType(entity.getRiskType() != null ? RouteRisk.RiskTypeEnum.fromValue(entity.getRiskType().name()) : null)
                .description(entity.getDescription());
        if (entity.getProbability() != null) {
            risk.setProbability(entity.getProbability().floatValue());
        }
        if (entity.getSeverity() != null) {
            risk.setSeverity(RouteRisk.SeverityEnum.fromValue(entity.getSeverity().name()));
        }
        return risk;
    }

    public Insurance toInsurance(LogisticsInsuranceEntity entity) {
        Insurance insurance = new Insurance()
                .insuranceId(entity.getId())
                .coveragePercentage(entity.getCoveragePercentage())
                .maxCoverage(entity.getMaxCoverage())
                .cost(entity.getCost())
                .shipmentId(entity.getShipment().getId());
        if (entity.getPlan() != null && entity.getPlan() != LogisticsInsurancePlan.NONE) {
            insurance.setPlan(Insurance.PlanEnum.fromValue(entity.getPlan().name()));
        }
        return insurance;
    }

    public InsurancePlan toInsurancePlan(LogisticsInsurancePlanEntity entity) {
        InsurancePlan plan = new InsurancePlan()
                .plan(InsurancePlan.PlanEnum.fromValue(entity.getPlan().name()))
                .coveragePercentage(entity.getCoveragePercentage())
                .maxCoverage(entity.getMaxCoverage())
                .description(entity.getDescription());
        if (entity.getCostPercentage() != null) {
            plan.setCostPercentage(entity.getCostPercentage().floatValue());
        }
        return plan;
    }

    public Vehicle toVehicle(LogisticsVehicleEntity entity) {
        Vehicle vehicle = new Vehicle()
                .type(entity.getVehicleType() != null ? Vehicle.TypeEnum.fromValue(entity.getVehicleType().name()) : null)
                .name(entity.getName());
        vehicle.setSpeedMultiplier(entity.getSpeedMultiplier());
        vehicle.setCapacityWeight(entity.getCapacityWeight());
        vehicle.setCapacityVolume(entity.getCapacityVolume());
        vehicle.setRiskModifier(entity.getRiskModifier());
        vehicle.setCostMultiplier(entity.getCostMultiplier());
        return vehicle;
    }

    public Incident toIncident(LogisticsIncidentEntity entity, List<LogisticsIncidentCargoLossEntity> losses) {
        Incident incident = new Incident()
                .incidentId(entity.getId())
                .type(Incident.TypeEnum.fromValue(entity.getType().name()))
                .severity(entity.getSeverity() != null ? entity.getSeverity().name() : null)
                .description(entity.getDescription())
                .resolved(entity.isResolved())
                .insuranceClaim(entity.isInsuranceClaim());
        if (entity.getOccurredAt() != null) {
            incident.setOccurredAt(entity.getOccurredAt());
        }
        incident.setCargoLost(losses.stream()
                .map(loss -> new IncidentCargoLostInner()
                        .itemId(loss.getItemId())
                        .quantity(loss.getQuantity()))
                .collect(Collectors.toList()));
        return incident;
    }

    public ShipmentDetailedAllOfTrackingHistory toTrackingHistory(LogisticsTrackingEventEntity entity) {
        ShipmentDetailedAllOfTrackingHistory history = new ShipmentDetailedAllOfTrackingHistory()
                .location(entity.getLocation())
                .event(entity.getEvent());
        if (entity.getOccurredAt() != null) {
            history.setTimestamp(entity.getOccurredAt());
        }
        return history;
    }

    public Convoy toConvoy(LogisticsConvoyEntity convoy,
                           List<LogisticsConvoyMemberEntity> members,
                           List<LogisticsConvoyShipmentEntity> shipments) {
        Convoy model = new Convoy()
                .convoyId(convoy.getId())
                .leaderId(convoy.getLeaderId())
                .status(convoy.getStatus().name());
        if (convoy.getRiskReduction() != null) {
            model.setRiskReduction(convoy.getRiskReduction());
        }
        model.setMembers(members.stream()
                .map(LogisticsConvoyMemberEntity::getCharacterId)
                .collect(Collectors.toList()));
        model.setShipments(shipments.stream()
                .map(item -> item.getShipment().getId())
                .collect(Collectors.toList()));
        return model;
    }

    public GetCharacterShipments200Response toCharacterShipmentsResponse(List<Shipment> shipments) {
        return new GetCharacterShipments200Response().shipments(shipments);
    }

    public GetRoutes200Response toRoutesResponse(List<Route> routes) {
        return new GetRoutes200Response().routes(routes);
    }

    public GetInsurancePlans200Response toInsurancePlansResponse(List<InsurancePlan> plans) {
        return new GetInsurancePlans200Response().plans(plans);
    }

    public GetVehicles200Response toVehiclesResponse(List<Vehicle> vehicles) {
        return new GetVehicles200Response().vehicles(vehicles);
    }

    private void fillShipmentBase(LogisticsShipmentEntity entity, Shipment shipment) {
        shipment.shipmentId(entity.getId());
        shipment.setCharacterId(entity.getCharacterId());
        shipment.setOrigin(entity.getOrigin());
        shipment.setDestination(entity.getDestination());
        shipment.setVehicleType(entity.getVehicleType().name());
        if (entity.getEstimatedDelivery() != null) {
            shipment.setEstimatedDelivery(entity.getEstimatedDelivery());
        }
        if (entity.getActualDelivery() != null) {
            shipment.setActualDelivery(JsonNullable.of(entity.getActualDelivery()));
        } else {
            shipment.setActualDelivery(JsonNullable.undefined());
        }
        if (entity.getCreatedAt() != null) {
            shipment.setCreatedAt(entity.getCreatedAt());
        }
        Shipment.StatusEnum status = mapShipmentStatus(entity.getStatus());
        if (status != null) {
            shipment.setStatus(status);
        }
    }

    private void fillShipmentBase(LogisticsShipmentEntity entity, ShipmentDetailed detailed) {
        detailed.shipmentId(entity.getId());
        detailed.setCharacterId(entity.getCharacterId());
        detailed.setOrigin(entity.getOrigin());
        detailed.setDestination(entity.getDestination());
        detailed.setVehicleType(entity.getVehicleType().name());
        if (entity.getEstimatedDelivery() != null) {
            detailed.setEstimatedDelivery(entity.getEstimatedDelivery());
        }
        if (entity.getActualDelivery() != null) {
            detailed.setActualDelivery(JsonNullable.of(entity.getActualDelivery()));
        } else {
            detailed.setActualDelivery(JsonNullable.undefined());
        }
        if (entity.getCreatedAt() != null) {
            detailed.setCreatedAt(entity.getCreatedAt());
        }
        ShipmentDetailed.StatusEnum status = mapShipmentDetailedStatus(entity.getStatus());
        if (status != null) {
            detailed.setStatus(status);
        }
    }

    private Shipment.StatusEnum mapShipmentStatus(LogisticsShipmentStatus status) {
        if (status == null) {
            return null;
        }
        return Shipment.StatusEnum.fromValue(status.name());
    }

    private ShipmentDetailed.StatusEnum mapShipmentDetailedStatus(LogisticsShipmentStatus status) {
        if (status == null) {
            return null;
        }
        return ShipmentDetailed.StatusEnum.fromValue(status.name());
    }

    private boolean isUuid(String value) {
        if (value == null) {
            return false;
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
