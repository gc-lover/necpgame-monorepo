package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С„СЂР°РєС†РёСЏРјРё (СЃРїСЂР°РІРѕС‡РЅРёРє)
 */
@Repository
public interface FactionRepository extends JpaRepository<FactionEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё С„СЂР°РєС†РёСЋ РїРѕ ID
     */
    Optional<FactionEntity> findById(UUID id);
    
    /**
     * РќР°Р№С‚Рё С„СЂР°РєС†РёРё, РґРѕСЃС‚СѓРїРЅС‹Рµ РґР»СЏ РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏ
     */
    @Query("SELECT f FROM FactionEntity f " +
           "JOIN f.origins o " +
           "WHERE o.originCode = :originCode")
    List<FactionEntity> findByAvailableForOrigin(@Param("originCode") String originCode);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ С„СЂР°РєС†РёРё
     */
    List<FactionEntity> findAll();
}

