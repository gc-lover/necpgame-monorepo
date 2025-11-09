package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CyberpsychosisTreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РјРµС‚РѕРґР°РјРё Р»РµС‡РµРЅРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Repository
public interface CyberpsychosisTreatmentRepository extends JpaRepository<CyberpsychosisTreatmentEntity, String> {
    
    /**
     * РќР°Р№С‚Рё Р»РµС‡РµРЅРёРµ РїРѕ С‚РёРїСѓ.
     */
    List<CyberpsychosisTreatmentEntity> findByType(CyberpsychosisTreatmentEntity.TreatmentType type);
    
    /**
     * РќР°Р№С‚Рё РґРѕСЃС‚СѓРїРЅРѕРµ Р»РµС‡РµРЅРёРµ РґР»СЏ СЃС‚Р°РґРёРё.
     */
    @Query("SELECT t FROM CyberpsychosisTreatmentEntity t WHERE t.requiredStage = :stage OR t.requiredStage IS NULL")
    List<CyberpsychosisTreatmentEntity> findAvailableForStage(CyberpsychosisTreatmentEntity.Stage stage);
}

