package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.*;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.repository.*;
import com.necpgame.backjava.service.CyberpsychosisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Р РµР°Р»РёР·Р°С†РёСЏ СЃРµСЂРІРёСЃР° РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёСЃС‚РµРјРѕР№ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CyberpsychosisServiceImpl implements CyberpsychosisService {
    
    private final CharacterRepository characterRepository;
    private final CharacterHumanityRepository humanityRepository;
    private final CyberpsychosisSymptomRepository symptomRepository;
    private final CharacterActiveSymptomRepository activeSymptomRepository;
    private final CyberpsychosisTreatmentRepository treatmentRepository;
    
    // ===== РЈРїСЂР°РІР»РµРЅРёРµ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊСЋ =====
    
    @Override
    @Transactional(readOnly = true)
    public HumanityInfo getHumanity(UUID playerId) {
        log.info("Getting humanity for player: {}", playerId);
        
        CharacterHumanityEntity humanity = getOrCreateHumanity(playerId);
        
        HumanityInfo info = new HumanityInfo();
        info.setCurrent(humanity.getCurrentHumanity());
        info.setMax(humanity.getMaxHumanity());
        info.setLossPercentage(humanity.getLossPercentage());
        info.setStage(HumanityInfo.StageEnum.fromValue(humanity.getStage().name()));
        
        return info;
    }
    
    @Override
    @Transactional(readOnly = true)
    public HumanityLossCalculation calculateHumanityLoss(UUID playerId, CalculateLossRequest request) {
        log.info("Calculating humanity loss for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public HumanityUpdateResult applyHumanityLoss(UUID playerId, ApplyLossRequest request) {
        log.info("Applying humanity loss for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    // ===== РЎС‚Р°РґРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    @Transactional(readOnly = true)
    public CyberpsychosisStage getCyberpsychosisStage(UUID playerId) {
        log.info("Getting cyberpsychosis stage for player: {}", playerId);
        
        CharacterHumanityEntity humanity = getOrCreateHumanity(playerId);
        
        CyberpsychosisStage stage = new CyberpsychosisStage();
        stage.setStage(CyberpsychosisStage.StageEnum.fromValue(humanity.getStage().name()));
        
        CyberpsychosisStageHumanityRange range = new CyberpsychosisStageHumanityRange();
        range.setMin(getStageMinHumanity(humanity.getStage()));
        range.setMax(getStageMaxHumanity(humanity.getStage()));
        stage.setHumanityRange(range);
        
        stage.setSymptoms(new ArrayList<>());
        stage.setEffects(new ArrayList<>());
        
        return stage;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Symptom> getSymptoms(UUID playerId) {
        log.info("Getting symptoms for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return new ArrayList<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public StageInfo getStageInfo(String stageId) {
        log.info("Getting stage info: {}", stageId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    // ===== РџСЂРѕРіСЂРµСЃСЃРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    @Transactional(readOnly = true)
    public ProgressionInfo getProgression(UUID playerId) {
        log.info("Getting progression for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProgressionCalculation calculateProgression(UUID playerId, CalculateProgressionRequest request) {
        log.info("Calculating progression for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public ProgressionTriggerResult triggerProgression(UUID playerId, TriggerProgressionRequest request) {
        log.info("Triggering progression for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    // ===== РџРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    @Override
    @Transactional(readOnly = true)
    public ConsequencesInfo getConsequences(UUID playerId) {
        log.info("Getting consequences for player: {}", playerId);
        
        ConsequencesInfo info = new ConsequencesInfo();
        info.setStatPenalties(new StatPenalties());
        info.setSocialEffects(new SocialEffects());
        
        return info;
    }
    
    @Override
    @Transactional(readOnly = true)
    public StatPenalties getStatPenalties(UUID playerId) {
        log.info("Getting stat penalties for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return new StatPenalties();
    }
    
    @Override
    @Transactional(readOnly = true)
    public SocialEffects getSocialEffects(UUID playerId) {
        log.info("Getting social effects for player: {}", playerId);
        
        // TODO: Р РµР°Р»РёР·РѕРІР°С‚СЊ РїРѕР»СѓС‡РµРЅРёРµ СЃРѕС†РёР°Р»СЊРЅС‹С… СЌС„С„РµРєС‚РѕРІ
        
        return new SocialEffects();
    }
    
    // ===== РЈРїСЂР°РІР»РµРЅРёРµ РєРёР±РµСЂРїСЃРёС…РѕР·РѕРј =====
    
    @Override
    @Transactional
    public PreventionResult applyPrevention(UUID playerId, ApplyPreventionRequest request) {
        log.info("Applying prevention for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public TreatmentResult applyTreatment(UUID playerId, ApplyTreatmentRequest request) {
        log.info("Applying treatment for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public GetTreatments200Response getTreatments(UUID playerId) {
        log.info("Getting treatments for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public SymptomManagementResult applySymptomManagement(UUID playerId, ApplySymptomManagementRequest request) {
        log.info("Applying symptom management for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public AdaptationInfo getAdaptation(UUID playerId) {
        log.info("Getting adaptation info for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public ImplantRemovalResult removeImplant(UUID playerId, RemoveImplantRequest request) {
        log.info("Removing implant for player: {}, implantId: {}", playerId, request.getImplantId());
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public DetoxificationResult performDetoxification(UUID playerId, DetoxificationRequest request) {
        log.info("Performing detoxification for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public TreatmentCosts getTreatmentCosts(UUID playerId, String treatmentType) {
        log.info("Getting treatment costs for player: {}, type: {}", playerId, treatmentType);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    @Override
    @Transactional
    public SocialSupportResult applySocialSupport(UUID playerId, ApplySocialSupportRequest request) {
        log.info("Applying social support for player: {}", playerId);
        
        // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
        return null;
    }
    
    // ===== Helper methods =====
    
    private CharacterHumanityEntity getOrCreateHumanity(UUID playerId) {
        return humanityRepository.findByCharacterId(playerId)
            .orElseGet(() -> {
                CharacterEntity character = characterRepository.findById(playerId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Character not found: " + playerId));
                
                CharacterHumanityEntity humanity = new CharacterHumanityEntity();
                humanity.setCharacter(character);
                humanity.setCurrentHumanity(100.0f);
                humanity.setMaxHumanity(100.0f);
                humanity.setLossPercentage(0.0f);
                humanity.setStage(CharacterHumanityEntity.CyberpsychosisStage.early);
                humanity.setTotalHumanityLost(0.0f);
                humanity.setAdaptationLevel(0);
                
                return humanityRepository.save(humanity);
            });
    }
    
    private CharacterHumanityEntity.CyberpsychosisStage calculateStage(Float humanity) {
        if (humanity >= 75) return CharacterHumanityEntity.CyberpsychosisStage.early;
        if (humanity >= 50) return CharacterHumanityEntity.CyberpsychosisStage.middle;
        if (humanity >= 25) return CharacterHumanityEntity.CyberpsychosisStage.late;
        return CharacterHumanityEntity.CyberpsychosisStage.cyberpsychosis;
    }
    
    private Float getStageMinHumanity(CharacterHumanityEntity.CyberpsychosisStage stage) {
        return switch (stage) {
            case early -> 75.0f;
            case middle -> 50.0f;
            case late -> 25.0f;
            case cyberpsychosis -> 0.0f;
        };
    }
    
    private Float getStageMaxHumanity(CharacterHumanityEntity.CyberpsychosisStage stage) {
        return switch (stage) {
            case early -> 100.0f;
            case middle -> 75.0f;
            case late -> 50.0f;
            case cyberpsychosis -> 25.0f;
        };
    }
    
    private String calculateRiskLevel(CharacterHumanityEntity humanity) {
        if (humanity.getLossPercentage() < 25) return "low";
        if (humanity.getLossPercentage() < 50) return "medium";
        if (humanity.getLossPercentage() < 75) return "high";
        return "critical";
    }
}

