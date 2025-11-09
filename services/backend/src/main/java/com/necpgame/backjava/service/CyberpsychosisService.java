package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;
import java.util.List;

/**
 * CyberpsychosisService - СЃРµСЂРІРёСЃ РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёСЃС‚РµРјРѕР№ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
public interface CyberpsychosisService {

    // ===== РЈРїСЂР°РІР»РµРЅРёРµ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊСЋ =====
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ С‚РµРєСѓС‰РёР№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РёРіСЂРѕРєР°.
     */
    HumanityInfo getHumanity(UUID playerId);
    
    /**
     * Р Р°СЃСЃС‡РёС‚Р°С‚СЊ РїРѕС‚РµСЂСЋ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РѕС‚ РёРјРїР»Р°РЅС‚Р°.
     */
    HumanityLossCalculation calculateHumanityLoss(UUID playerId, CalculateLossRequest request);
    
    /**
     * РџСЂРёРјРµРЅРёС‚СЊ РїРѕС‚РµСЂСЋ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё.
     */
    HumanityUpdateResult applyHumanityLoss(UUID playerId, ApplyLossRequest request);
    
    // ===== РЎС‚Р°РґРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ С‚РµРєСѓС‰СѓСЋ СЃС‚Р°РґРёСЋ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    CyberpsychosisStage getCyberpsychosisStage(UUID playerId);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРїРёСЃРѕРє СЃРёРјРїС‚РѕРјРѕРІ С‚РµРєСѓС‰РµР№ СЃС‚Р°РґРёРё.
     */
    List<Symptom> getSymptoms(UUID playerId);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ РєРѕРЅРєСЂРµС‚РЅРѕР№ СЃС‚Р°РґРёРё.
     */
    StageInfo getStageInfo(String stageId);
    
    // ===== РџСЂРѕРіСЂРµСЃСЃРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ РїСЂРѕРіСЂРµСЃСЃРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    ProgressionInfo getProgression(UUID playerId);
    
    /**
     * Р Р°СЃСЃС‡РёС‚Р°С‚СЊ СЂРёСЃРє РїСЂРѕРіСЂРµСЃСЃРёРё.
     */
    ProgressionCalculation calculateProgression(UUID playerId, CalculateProgressionRequest request);
    
    /**
     * РўСЂРёРіРіРµСЂРЅСѓС‚СЊ РїСЂРѕРіСЂРµСЃСЃРёСЋ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    ProgressionTriggerResult triggerProgression(UUID playerId, TriggerProgressionRequest request);
    
    // ===== РџРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° =====
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ РїРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    ConsequencesInfo getConsequences(UUID playerId);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ С€С‚СЂР°С„С‹ Рє С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєР°Рј.
     */
    StatPenalties getStatPenalties(UUID playerId);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРѕС†РёР°Р»СЊРЅС‹Рµ СЌС„С„РµРєС‚С‹ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    SocialEffects getSocialEffects(UUID playerId);
    
    // ===== РЈРїСЂР°РІР»РµРЅРёРµ РєРёР±РµСЂРїСЃРёС…РѕР·РѕРј =====
    
    /**
     * РџСЂРёРјРµРЅРёС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєСѓ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    PreventionResult applyPrevention(UUID playerId, ApplyPreventionRequest request);
    
    /**
     * РџСЂРёРјРµРЅРёС‚СЊ Р»РµС‡РµРЅРёРµ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    TreatmentResult applyTreatment(UUID playerId, ApplyTreatmentRequest request);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РґРѕСЃС‚СѓРїРЅС‹Рµ РјРµС‚РѕРґС‹ Р»РµС‡РµРЅРёСЏ.
     */
    GetTreatments200Response getTreatments(UUID playerId);
    
    /**
     * РџСЂРёРјРµРЅРёС‚СЊ СѓРїСЂР°РІР»РµРЅРёРµ СЃРёРјРїС‚РѕРјР°РјРё.
     */
    SymptomManagementResult applySymptomManagement(UUID playerId, ApplySymptomManagementRequest request);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РёРЅС„РѕСЂРјР°С†РёСЋ РѕР± Р°РґР°РїС‚Р°С†РёРё Рє РєРёР±РµСЂРїСЃРёС…РѕР·Сѓ.
     */
    AdaptationInfo getAdaptation(UUID playerId);
    
    /**
     * РЈРґР°Р»РёС‚СЊ РёРјРїР»Р°РЅС‚ РґР»СЏ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё.
     */
    ImplantRemovalResult removeImplant(UUID playerId, RemoveImplantRequest request);
    
    /**
     * Р’С‹РїРѕР»РЅРёС‚СЊ РґРµС‚РѕРєСЃРёРєР°С†РёСЋ.
     */
    DetoxificationResult performDetoxification(UUID playerId, DetoxificationRequest request);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ.
     */
    TreatmentCosts getTreatmentCosts(UUID playerId, String treatmentType);
    
    /**
     * РџСЂРёРјРµРЅРёС‚СЊ СЃРѕС†РёР°Р»СЊРЅСѓСЋ РїРѕРґРґРµСЂР¶РєСѓ РґР»СЏ СЃРЅРёР¶РµРЅРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°.
     */
    SocialSupportResult applySocialSupport(UUID playerId, ApplySocialSupportRequest request);
}

