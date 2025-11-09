package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TutorialProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * TutorialProgressRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїСЂРѕРіСЂРµСЃСЃРѕРј С‚СѓС‚РѕСЂРёР°Р»Р°.
 */
@Repository
public interface TutorialProgressRepository extends JpaRepository<TutorialProgressEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РїСЂРѕРіСЂРµСЃСЃ С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return РїСЂРѕРіСЂРµСЃСЃ С‚СѓС‚РѕСЂРёР°Р»Р°
     */
    Optional<TutorialProgressEntity> findByCharacterId(UUID characterId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ РїСЂРѕРіСЂРµСЃСЃР° С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return true, РµСЃР»Рё РїСЂРѕРіСЂРµСЃСЃ СЃСѓС‰РµСЃС‚РІСѓРµС‚
     */
    boolean existsByCharacterId(UUID characterId);

    /**
     * РЈРґР°Р»РёС‚СЊ РїСЂРѕРіСЂРµСЃСЃ С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     */
    void deleteByCharacterId(UUID characterId);
}

