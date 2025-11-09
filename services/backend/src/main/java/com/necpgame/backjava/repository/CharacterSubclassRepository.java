package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSubclassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїРѕРґРєР»Р°СЃСЃР°РјРё РїРµСЂСЃРѕРЅР°Р¶РµР№ (СЃРїСЂР°РІРѕС‡РЅРёРє)
 */
@Repository
public interface CharacterSubclassRepository extends JpaRepository<CharacterSubclassEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РїРѕРґРєР»Р°СЃСЃ РїРѕ РєРѕРґСѓ
     */
    Optional<CharacterSubclassEntity> findBySubclassCode(String subclassCode);
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РїРѕРґРєР»Р°СЃСЃС‹ РґР»СЏ РєР»Р°СЃСЃР°
     */
    List<CharacterSubclassEntity> findAllByCharacterClass_ClassCode(String classCode);
}

