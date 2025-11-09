package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CyberpsychosisSymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё РєРёР±РµСЂРїСЃРёС…РѕР·Р° (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Repository
public interface CyberpsychosisSymptomRepository extends JpaRepository<CyberpsychosisSymptomEntity, String> {
    
    /**
     * РќР°Р№С‚Рё СЃРёРјРїС‚РѕРјС‹ РїРѕ СЃС‚Р°РґРёРё.
     */
    List<CyberpsychosisSymptomEntity> findByStage(CyberpsychosisSymptomEntity.Stage stage);
    
    /**
     * РќР°Р№С‚Рё СЃРёРјРїС‚РѕРјС‹ РїРѕ СЃРµСЂСЊРµР·РЅРѕСЃС‚Рё.
     */
    List<CyberpsychosisSymptomEntity> findBySeverity(CyberpsychosisSymptomEntity.Severity severity);
    
    /**
     * РќР°Р№С‚Рё СЃРёРјРїС‚РѕРјС‹ РїРѕ РєР°С‚РµРіРѕСЂРёРё.
     */
    List<CyberpsychosisSymptomEntity> findByCategory(CyberpsychosisSymptomEntity.Category category);
    
    /**
     * РќР°Р№С‚Рё СЃРёРјРїС‚РѕРјС‹ РґР»СЏ СЃС‚Р°РґРёРё Рё СЃРµСЂСЊРµР·РЅРѕСЃС‚Рё.
     */
    @Query("SELECT s FROM CyberpsychosisSymptomEntity s WHERE s.stage = :stage AND s.severity = :severity")
    List<CyberpsychosisSymptomEntity> findByStageAndSeverity(
        CyberpsychosisSymptomEntity.Stage stage,
        CyberpsychosisSymptomEntity.Severity severity
    );
}

