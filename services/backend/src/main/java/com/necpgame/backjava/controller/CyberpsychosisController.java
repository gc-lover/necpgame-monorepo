package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayCyberpsychosisApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.CyberpsychosisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ СЃРёСЃС‚РµРјРѕР№ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link GameplayCyberpsychosisApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CyberpsychosisController implements GameplayCyberpsychosisApi {
    
    private final CyberpsychosisService service;
    
    // ===== РЈРїСЂР°РІР»РµРЅРёРµ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊСЋ =====
    
    @Override
    public ResponseEntity<HumanityInfo> getHumanity(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/humanity", playerId);
        return ResponseEntity.ok(service.getHumanity(playerId));
    }
    
    @Override
    public ResponseEntity<HumanityLossCalculation> calculateHumanityLoss(UUID playerId, CalculateLossRequest calculateLossRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/humanity/calculate-loss", playerId);
        return ResponseEntity.ok(service.calculateHumanityLoss(playerId, calculateLossRequest));
    }
    
    @Override
    public ResponseEntity<HumanityUpdateResult> applyHumanityLoss(UUID playerId, ApplyLossRequest applyLossRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/humanity/apply-loss", playerId);
        return ResponseEntity.ok(service.applyHumanityLoss(playerId, applyLossRequest));
    }
    
    // ===== РЎС‚Р°РґРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    public ResponseEntity<CyberpsychosisStage> getCyberpsychosisStage(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/stage", playerId);
        return ResponseEntity.ok(service.getCyberpsychosisStage(playerId));
    }
    
    @Override
    public ResponseEntity<List<Symptom>> getSymptoms(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/symptoms", playerId);
        return ResponseEntity.ok(service.getSymptoms(playerId));
    }
    
    @Override
    public ResponseEntity<StageInfo> getStageInfo(String stageId) {
        log.info("GET /gameplay/combat/cyberpsychosis/stages/{}", stageId);
        return ResponseEntity.ok(service.getStageInfo(stageId));
    }
    
    // ===== РџСЂРѕРіСЂРµСЃСЃРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    public ResponseEntity<ProgressionInfo> getProgression(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/progression", playerId);
        return ResponseEntity.ok(service.getProgression(playerId));
    }
    
    @Override
    public ResponseEntity<ProgressionCalculation> calculateProgression(UUID playerId, CalculateProgressionRequest calculateProgressionRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/progression/calculate", playerId);
        return ResponseEntity.ok(service.calculateProgression(playerId, calculateProgressionRequest));
    }
    
    @Override
    public ResponseEntity<ProgressionTriggerResult> triggerProgression(UUID playerId, TriggerProgressionRequest triggerProgressionRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/progression/trigger", playerId);
        return ResponseEntity.ok(service.triggerProgression(playerId, triggerProgressionRequest));
    }
    
    // ===== РџРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    public ResponseEntity<ConsequencesInfo> getConsequences(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/consequences", playerId);
        return ResponseEntity.ok(service.getConsequences(playerId));
    }
    
    @Override
    public ResponseEntity<StatPenalties> getStatPenalties(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/stat-penalties", playerId);
        return ResponseEntity.ok(service.getStatPenalties(playerId));
    }
    
    @Override
    public ResponseEntity<SocialEffects> getSocialEffects(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/social-effects", playerId);
        return ResponseEntity.ok(service.getSocialEffects(playerId));
    }
    
    // ===== РЈРїСЂР°РІР»РµРЅРёРµ РєРёР±РµСЂРїСЃРёС…РѕР·РѕРј =====
    
    @Override
    public ResponseEntity<PreventionResult> applyPrevention(UUID playerId, ApplyPreventionRequest applyPreventionRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/prevention", playerId);
        return ResponseEntity.ok(service.applyPrevention(playerId, applyPreventionRequest));
    }
    
    @Override
    public ResponseEntity<TreatmentResult> applyTreatment(UUID playerId, ApplyTreatmentRequest applyTreatmentRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/treatment", playerId);
        return ResponseEntity.ok(service.applyTreatment(playerId, applyTreatmentRequest));
    }
    
    // TODO: Р’СЂРµРјРµРЅРЅРѕ Р·Р°РєРѕРјРјРµРЅС‚РёСЂРѕРІР°РЅРѕ РёР·-Р·Р° РЅРµСЃРѕРѕС‚РІРµС‚СЃС‚РІРёСЏ API РёРЅС‚РµСЂС„РµР№СЃСѓ
    // @Override
    // public ResponseEntity<GetTreatments200Response> getTreatments(UUID playerId) {
    //     log.info("GET /gameplay/combat/cyberpsychosis/{}/treatments", playerId);
    //     return ResponseEntity.ok(service.getTreatments(playerId));
    // }
    
    @Override
    public ResponseEntity<SymptomManagementResult> applySymptomManagement(UUID playerId, ApplySymptomManagementRequest applySymptomManagementRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/symptom-management", playerId);
        return ResponseEntity.ok(service.applySymptomManagement(playerId, applySymptomManagementRequest));
    }
    
    @Override
    public ResponseEntity<AdaptationInfo> getAdaptation(UUID playerId) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/adaptation", playerId);
        return ResponseEntity.ok(service.getAdaptation(playerId));
    }
    
    @Override
    public ResponseEntity<ImplantRemovalResult> removeImplant(UUID playerId, RemoveImplantRequest removeImplantRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/implant-removal", playerId);
        return ResponseEntity.ok(service.removeImplant(playerId, removeImplantRequest));
    }
    
    @Override
    public ResponseEntity<DetoxificationResult> performDetoxification(UUID playerId, DetoxificationRequest detoxificationRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/detoxification", playerId);
        return ResponseEntity.ok(service.performDetoxification(playerId, detoxificationRequest));
    }
    
    @Override
    public ResponseEntity<TreatmentCosts> getTreatmentCosts(UUID playerId, String treatmentType) {
        log.info("GET /gameplay/combat/cyberpsychosis/{}/treatment-costs", playerId);
        return ResponseEntity.ok(service.getTreatmentCosts(playerId, treatmentType));
    }
    
    @Override
    public ResponseEntity<SocialSupportResult> applySocialSupport(UUID playerId, ApplySocialSupportRequest applySocialSupportRequest) {
        log.info("POST /gameplay/combat/cyberpsychosis/{}/social-support", playerId);
        return ResponseEntity.ok(service.applySocialSupport(playerId, applySocialSupportRequest));
    }
}

