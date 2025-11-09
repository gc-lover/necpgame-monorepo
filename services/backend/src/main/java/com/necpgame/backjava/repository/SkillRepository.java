package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SkillRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃРїСЂР°РІРѕС‡РЅРёРєРѕРј РЅР°РІС‹РєРѕРІ.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml
 */
@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, String> {

    /**
     * РќР°Р№С‚Рё РЅР°РІС‹РєРё РїРѕ РєР°С‚РµРіРѕСЂРёРё.
     */
    @Query("SELECT s FROM SkillEntity s WHERE s.category = :category ORDER BY s.name")
    List<SkillEntity> findByCategory(String category);

    /**
     * РќР°Р№С‚Рё РІСЃРµ РЅР°РІС‹РєРё, РѕС‚СЃРѕСЂС‚РёСЂРѕРІР°РЅРЅС‹Рµ РїРѕ РёРјРµРЅРё.
     */
    @Query("SELECT s FROM SkillEntity s ORDER BY s.category, s.name")
    List<SkillEntity> findAllOrderByName();
}

