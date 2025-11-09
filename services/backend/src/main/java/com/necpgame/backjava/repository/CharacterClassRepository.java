package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РєР»Р°СЃСЃР°РјРё РїРµСЂСЃРѕРЅР°Р¶РµР№ (СЃРїСЂР°РІРѕС‡РЅРёРє)
 */
@Repository
public interface CharacterClassRepository extends JpaRepository<CharacterClassEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РєР»Р°СЃСЃ РїРѕ РєРѕРґСѓ
     */
    Optional<CharacterClassEntity> findByClassCode(String classCode);
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ РІСЃРµ РєР»Р°СЃСЃС‹ СЃ РїРѕРґРєР»Р°СЃСЃР°РјРё
     */
    @Query("SELECT DISTINCT c FROM CharacterClassEntity c LEFT JOIN FETCH c.subclasses")
    List<CharacterClassEntity> findAllWithSubclasses();
}

