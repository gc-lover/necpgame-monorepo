package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.ImplantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°РјРё (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Repository
public interface ImplantRepository extends JpaRepository<ImplantEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РёРјРїР»Р°РЅС‚С‹ РїРѕ С‚РёРїСѓ.
     */
    List<ImplantEntity> findByType(ImplantEntity.ImplantType type);
    
    /**
     * РќР°Р№С‚Рё РёРјРїР»Р°РЅС‚С‹ РїРѕ С‚РёРїСѓ СЃР»РѕС‚Р°.
     */
    List<ImplantEntity> findBySlotType(ImplantEntity.SlotType slotType);
    
    /**
     * РќР°Р№С‚Рё РёРјРїР»Р°РЅС‚С‹ РїРѕ СЂРµРґРєРѕСЃС‚Рё.
     */
    List<ImplantEntity> findByRarity(ImplantEntity.Rarity rarity);
    
    /**
     * РќР°Р№С‚Рё РёРјРїР»Р°РЅС‚С‹ РґРѕСЃС‚СѓРїРЅС‹Рµ РґР»СЏ СѓСЂРѕРІРЅСЏ.
     */
    @Query("SELECT i FROM ImplantEntity i WHERE i.minLevel <= :level")
    List<ImplantEntity> findAvailableForLevel(Integer level);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ РёРјРїР»Р°РЅС‚Р°.
     */
    boolean existsById(String id);
}

