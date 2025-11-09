package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.GameLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GameLocationRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРіСЂРѕРІС‹РјРё Р»РѕРєР°С†РёСЏРјРё.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/locations/locations.yaml
 */
@Repository
public interface GameLocationRepository extends JpaRepository<GameLocationEntity, String> {

    /**
     * РќР°Р№С‚Рё РґРѕСЃС‚СѓРїРЅС‹Рµ Р»РѕРєР°С†РёРё.
     */
    @Query("SELECT l FROM GameLocationEntity l WHERE l.accessible = true ORDER BY l.name")
    List<GameLocationEntity> findAccessibleLocations();

    /**
     * РќР°Р№С‚Рё Р»РѕРєР°С†РёРё РїРѕ С‚РёРїСѓ.
     */
    @Query("SELECT l FROM GameLocationEntity l WHERE l.locationType = :type ORDER BY l.dangerLevel, l.name")
    List<GameLocationEntity> findByType(String type);

    /**
     * РќР°Р№С‚Рё Р»РѕРєР°С†РёРё РїРѕ СѓСЂРѕРІРЅСЋ РѕРїР°СЃРЅРѕСЃС‚Рё.
     */
    @Query("SELECT l FROM GameLocationEntity l WHERE l.dangerLevel <= :maxDanger ORDER BY l.dangerLevel, l.name")
    List<GameLocationEntity> findByDangerLevelLessThanEqual(Integer maxDanger);
}

