package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LogisticsCargoItemEntity;
import com.necpgame.backjava.entity.LogisticsInsuranceEntity;
import com.necpgame.backjava.entity.LogisticsInsurancePlanEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import com.necpgame.backjava.model.GetInsurancePlans200Response;
import com.necpgame.backjava.model.Insurance;
import com.necpgame.backjava.model.InsurancePlan;
import com.necpgame.backjava.model.InsuranceRequest;
import com.necpgame.backjava.repository.LogisticsCargoItemRepository;
import com.necpgame.backjava.repository.LogisticsInsurancePlanRepository;
import com.necpgame.backjava.repository.LogisticsInsuranceRepository;
import com.necpgame.backjava.repository.LogisticsShipmentRepository;
import com.necpgame.backjava.service.InsuranceService;
import com.necpgame.backjava.service.mapper.LogisticsMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final LogisticsInsuranceRepository insuranceRepository;
    private final LogisticsInsurancePlanRepository planRepository;
    private final LogisticsShipmentRepository shipmentRepository;
    private final LogisticsCargoItemRepository cargoItemRepository;
    private final LogisticsMapper mapper;

    public InsuranceServiceImpl(LogisticsInsuranceRepository insuranceRepository,
                                LogisticsInsurancePlanRepository planRepository,
                                LogisticsShipmentRepository shipmentRepository,
                                LogisticsCargoItemRepository cargoItemRepository,
                                LogisticsMapper mapper) {
        this.insuranceRepository = insuranceRepository;
        this.planRepository = planRepository;
        this.shipmentRepository = shipmentRepository;
        this.cargoItemRepository = cargoItemRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public GetInsurancePlans200Response getInsurancePlans() {
        List<InsurancePlan> plans = planRepository.findAll().stream()
                .map(mapper::toInsurancePlan)
                .collect(Collectors.toList());
        return mapper.toInsurancePlansResponse(plans);
    }

    @Override
    public Insurance purchaseInsurance(InsuranceRequest insuranceRequest) {
        UUID shipmentId = requireShipmentId(insuranceRequest);
        LogisticsInsurancePlan plan = parsePlan(insuranceRequest.getPlan() != null ? insuranceRequest.getPlan().getValue() : null);

        LogisticsShipmentEntity shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        LogisticsInsurancePlanEntity planEntity = planRepository.findById(plan)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insurance plan not available"));

        List<LogisticsCargoItemEntity> cargo = cargoItemRepository.findByShipment(shipment);
        BigDecimal totalValue = cargo.stream()
                .map(LogisticsCargoItemEntity::getValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal costPercentage = planEntity.getCostPercentage() != null ? planEntity.getCostPercentage() : BigDecimal.ZERO;
        int cost = costPercentage.multiply(totalValue).setScale(0, RoundingMode.HALF_UP).intValue();

        LogisticsInsuranceEntity insurance = insuranceRepository.findByShipment(shipment)
                .orElse(LogisticsInsuranceEntity.builder().id(UUID.randomUUID()).shipment(shipment).build());
        insurance.setPlan(plan);
        insurance.setCoveragePercentage(planEntity.getCoveragePercentage());
        insurance.setMaxCoverage(planEntity.getMaxCoverage());
        insurance.setCost(cost);
        insurance.setPurchasedAt(OffsetDateTime.now());
        insurance.setTotalCargoValue(totalValue);
        insuranceRepository.save(insurance);

        shipment.setInsurancePlan(plan);
        shipment.setInsuranceCost(BigDecimal.valueOf(cost));
        shipmentRepository.save(shipment);

        return mapper.toInsurance(insurance);
    }

    private UUID requireShipmentId(InsuranceRequest insuranceRequest) {
        if (insuranceRequest.getShipmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing shipment_id");
        }
        return insuranceRequest.getShipmentId();
    }

    private LogisticsInsurancePlan parsePlan(String plan) {
        if (plan == null || plan.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing plan");
        }
        try {
            return LogisticsInsurancePlan.valueOf(plan.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid plan");
        }
    }
}
