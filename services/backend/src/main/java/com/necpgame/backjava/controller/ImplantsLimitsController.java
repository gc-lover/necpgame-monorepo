package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayImplantsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.ImplantsLimitsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РѕРіСЂР°РЅРёС‡РµРЅРёСЏРјРё Рё СЌРЅРµСЂРіРµС‚РёРєРѕР№ РёРјРїР»Р°РЅС‚РѕРІ.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link GameplayImplantsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ImplantsLimitsController implements GameplayImplantsApi {
    
    private final ImplantsLimitsService service;
    
    @Override
    public ResponseEntity<EnergyCalculation> calculateEnergyConsumption(UUID playerId, CalculateEnergyRequest calculateEnergyRequest) {
        log.info("POST /gameplay/combat/implants/{}/energy/calculate", playerId);
        return ResponseEntity.ok(service.calculateEnergyConsumption(playerId, calculateEnergyRequest));
    }
    
    @Override
    public ResponseEntity<ImplantLimitCalculation> calculateImplantLimit(UUID playerId, CalculateLimitRequest calculateLimitRequest) {
        log.info("POST /gameplay/combat/implants/{}/limit/calculate", playerId);
        return ResponseEntity.ok(service.calculateImplantLimit(playerId, calculateLimitRequest));
    }
    
    @Override
    public ResponseEntity<CompatibilityResult> checkCompatibility(UUID playerId, CompatibilityCheckRequest compatibilityCheckRequest) {
        log.info("POST /gameplay/combat/implants/{}/compatibility", playerId);
        return ResponseEntity.ok(service.checkCompatibility(playerId, compatibilityCheckRequest));
    }
    
    @Override
    public ResponseEntity<EnergyPoolInfo> getEnergyPool(UUID playerId) {
        log.info("GET /gameplay/combat/implants/{}/energy", playerId);
        return ResponseEntity.ok(service.getEnergyPool(playerId));
    }
    
    @Override
    public ResponseEntity<ImplantLimitInfo> getImplantLimit(UUID playerId) {
        log.info("GET /gameplay/combat/implants/{}/limit", playerId);
        return ResponseEntity.ok(service.getImplantLimit(playerId));
    }
    
    @Override
    public ResponseEntity<ImplantLimits> getImplantLimits(UUID playerId) {
        log.info("GET /gameplay/combat/implants/{}/limits", playerId);
        return ResponseEntity.ok(service.getImplantLimits(playerId));
    }
    
    @Override
    public ResponseEntity<ImplantSlots> getImplantSlots(UUID playerId, String type) {
        log.info("GET /gameplay/combat/implants/{}/slots?type={}", playerId, type);
        return ResponseEntity.ok(service.getImplantSlots(playerId, type));
    }
    
    // TODO: Р’СЂРµРјРµРЅРЅРѕ Р·Р°РєРѕРјРјРµРЅС‚РёСЂРѕРІР°РЅРѕ РёР·-Р·Р° РЅРµСЃРѕРѕС‚РІРµС‚СЃС‚РІРёСЏ API РёРЅС‚РµСЂС„РµР№СЃСѓ
    // @Override
    // public ResponseEntity<List<IndividualEnergyLimits>> getIndividualEnergyLimits(UUID playerId) {
    //     log.info("GET /gameplay/combat/implants/{}/energy/individual", playerId);
    //     return ResponseEntity.ok(service.getIndividualEnergyLimits(playerId));
    // }
    
    @Override
    public ResponseEntity<EnergyRestoreResult> restoreEnergy(UUID playerId, RestoreEnergyRequest restoreEnergyRequest) {
        log.info("POST /gameplay/combat/implants/{}/energy/restore", playerId);
        return ResponseEntity.ok(service.restoreEnergy(playerId, restoreEnergyRequest));
    }
    
    @Override
    public ResponseEntity<ValidationResult> validateInstall(UUID playerId, ValidateInstallRequest validateInstallRequest) {
        log.info("POST /gameplay/combat/implants/{}/validate-install", playerId);
        return ResponseEntity.ok(service.validateInstall(playerId, validateInstallRequest));
    }
}

