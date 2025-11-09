package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;
import java.util.List;

/**
 * ImplantsLimitsService - СЃРµСЂРІРёСЃ РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РѕРіСЂР°РЅРёС‡РµРЅРёСЏРјРё Рё СЌРЅРµСЂРіРµС‚РёРєРѕР№ РёРјРїР»Р°РЅС‚РѕРІ.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
public interface ImplantsLimitsService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РґРѕСЃС‚СѓРїРЅС‹Рµ СЃР»РѕС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ РёРіСЂРѕРєР°.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param type С„РёР»СЊС‚СЂ РїРѕ С‚РёРїСѓ СЃР»РѕС‚Р° (РѕРїС†РёРѕРЅР°Р»СЊРЅРѕ)
     * @return РёРЅС„РѕСЂРјР°С†РёСЏ Рѕ СЃР»РѕС‚Р°С… РёРјРїР»Р°РЅС‚РѕРІ
     */
    ImplantSlots getImplantSlots(UUID playerId, String type);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚СЊ РёРјРїР»Р°РЅС‚Р° СЃ С‚РµРєСѓС‰РёРјРё РёРјРїР»Р°РЅС‚Р°РјРё.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param request Р·Р°РїСЂРѕСЃ РЅР° РїСЂРѕРІРµСЂРєСѓ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё
     * @return СЂРµР·СѓР»СЊС‚Р°С‚ РїСЂРѕРІРµСЂРєРё СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё
     */
    CompatibilityResult checkCompatibility(UUID playerId, CompatibilityCheckRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ Р»РёРјРёС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ РёРіСЂРѕРєР°.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @return РёРЅС„РѕСЂРјР°С†РёСЏ Рѕ РІСЃРµС… Р»РёРјРёС‚Р°С…
     */
    ImplantLimits getImplantLimits(UUID playerId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ С‚РµРєСѓС‰РёР№ Р»РёРјРёС‚ РёРјРїР»Р°РЅС‚РѕРІ РёРіСЂРѕРєР°.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @return РёРЅС„РѕСЂРјР°С†РёСЏ Рѕ С‚РµРєСѓС‰РµРј Р»РёРјРёС‚Рµ
     */
    ImplantLimitInfo getImplantLimit(UUID playerId);

    /**
     * Р Р°СЃСЃС‡РёС‚Р°С‚СЊ Р»РёРјРёС‚ РёРјРїР»Р°РЅС‚РѕРІ СЃ СѓС‡РµС‚РѕРј РјРѕРґРёС„РёРєР°С‚РѕСЂРѕРІ.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param request Р·Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ Р»РёРјРёС‚Р°
     * @return СЂРµР·СѓР»СЊС‚Р°С‚ СЂР°СЃС‡РµС‚Р° Р»РёРјРёС‚Р°
     */
    ImplantLimitCalculation calculateImplantLimit(UUID playerId, CalculateLimitRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ РїСѓР» РёРіСЂРѕРєР°.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @return РёРЅС„РѕСЂРјР°С†РёСЏ РѕР± СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРј РїСѓР»Рµ
     */
    EnergyPoolInfo getEnergyPool(UUID playerId);

    /**
     * Р Р°СЃСЃС‡РёС‚Р°С‚СЊ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРµ РїРѕС‚СЂРµР±Р»РµРЅРёРµ РёРјРїР»Р°РЅС‚РѕРІ.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param request Р·Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ СЌРЅРµСЂРіРѕРїРѕС‚СЂРµР±Р»РµРЅРёСЏ
     * @return СЂРµР·СѓР»СЊС‚Р°С‚ СЂР°СЃС‡РµС‚Р° СЌРЅРµСЂРіРѕРїРѕС‚СЂРµР±Р»РµРЅРёСЏ
     */
    EnergyCalculation calculateEnergyConsumption(UUID playerId, CalculateEnergyRequest request);

    /**
     * Р’РѕСЃСЃС‚Р°РЅРѕРІРёС‚СЊ СЌРЅРµСЂРіРёСЋ.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param request Р·Р°РїСЂРѕСЃ РЅР° РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ СЌРЅРµСЂРіРёРё
     * @return СЂРµР·СѓР»СЊС‚Р°С‚ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё
     */
    EnergyRestoreResult restoreEnergy(UUID playerId, RestoreEnergyRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Рµ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёРµ Р»РёРјРёС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @return СЃРїРёСЃРѕРє РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹С… Р»РёРјРёС‚РѕРІ РёРјРїР»Р°РЅС‚РѕРІ
     */
    List<IndividualEnergyLimits> getIndividualEnergyLimits(UUID playerId);

    /**
     * Р’Р°Р»РёРґРёСЂРѕРІР°С‚СЊ СѓСЃС‚Р°РЅРѕРІРєСѓ РёРјРїР»Р°РЅС‚Р°.
     * 
     * @param playerId ID РёРіСЂРѕРєР°
     * @param request Р·Р°РїСЂРѕСЃ РЅР° РІР°Р»РёРґР°С†РёСЋ СѓСЃС‚Р°РЅРѕРІРєРё
     * @return СЂРµР·СѓР»СЊС‚Р°С‚ РІР°Р»РёРґР°С†РёРё
     */
    ValidationResult validateInstall(UUID playerId, ValidateInstallRequest request);
}

