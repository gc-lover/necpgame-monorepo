package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РіРѕСЂРѕРґР°РјРё (СЃРїСЂР°РІРѕС‡РЅРёРє)
 */
@Repository
public interface CityRepository extends JpaRepository<CityEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РіРѕСЂРѕРґ РїРѕ ID
     */
    Optional<CityEntity> findById(UUID id);
    
    /**
     * РќР°Р№С‚Рё РіРѕСЂРѕРґР° РїРѕ СЂРµРіРёРѕРЅСѓ
     */
    List<CityEntity> findByRegion(String region);
    
    /**
     * РќР°Р№С‚Рё РіРѕСЂРѕРґР°, РґРѕСЃС‚СѓРїРЅС‹Рµ РґР»СЏ С„СЂР°РєС†РёРё
     */
    @Query("SELECT c FROM CityEntity c " +
           "JOIN c.availableFactions f " +
           "WHERE f.id = :factionId")
    List<CityEntity> findByAvailableForFaction(@Param("factionId") UUID factionId);
    
    /**
     * РќР°Р№С‚Рё РіРѕСЂРѕРґР° РїРѕ СЂРµРіРёРѕРЅСѓ Рё С„СЂР°РєС†РёРё
     */
    @Query("SELECT c FROM CityEntity c " +
           "JOIN c.availableFactions f " +
           "WHERE c.region = :region AND f.id = :factionId")
    List<CityEntity> findByRegionAndFaction(@Param("region") String region, @Param("factionId") UUID factionId);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ РіРѕСЂРѕРґР°
     */
    List<CityEntity> findAll();
}

