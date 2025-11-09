package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterOriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏРјРё РїРµСЂСЃРѕРЅР°Р¶РµР№ (СЃРїСЂР°РІРѕС‡РЅРёРє)
 */
@Repository
public interface CharacterOriginRepository extends JpaRepository<CharacterOriginEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёРµ РїРѕ РєРѕРґСѓ
     */
    Optional<CharacterOriginEntity> findByOriginCode(String originCode);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏ СЃ РґРѕСЃС‚СѓРїРЅС‹РјРё С„СЂР°РєС†РёСЏРјРё
     */
    @Query("SELECT DISTINCT o FROM CharacterOriginEntity o LEFT JOIN FETCH o.availableFactions")
    List<CharacterOriginEntity> findAllWithFactions();
}

