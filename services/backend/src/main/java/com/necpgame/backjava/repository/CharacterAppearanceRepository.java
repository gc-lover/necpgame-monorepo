package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РІРЅРµС€РЅРѕСЃС‚СЊСЋ РїРµСЂСЃРѕРЅР°Р¶РµР№
 */
@Repository
public interface CharacterAppearanceRepository extends JpaRepository<CharacterAppearanceEntity, UUID> {
    // Р‘Р°Р·РѕРІС‹Рµ CRUD РѕРїРµСЂР°С†РёРё РѕС‚ JpaRepository
}

