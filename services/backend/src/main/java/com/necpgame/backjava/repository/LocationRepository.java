package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * LocationRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Р»РѕРєР°С†РёСЏРјРё.
 */
@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, String> {

    /**
     * РќР°Р№С‚Рё Р»РѕРєР°С†РёСЋ РїРѕ ID.
     *
     * @param id ID Р»РѕРєР°С†РёРё
     * @return Р»РѕРєР°С†РёСЏ
     */
    Optional<LocationEntity> findById(String id);

    /**
     * РќР°Р№С‚Рё РІСЃРµ Р»РѕРєР°С†РёРё РїРѕ РіРѕСЂРѕРґСѓ.
     *
     * @param city РіРѕСЂРѕРґ
     * @return СЃРїРёСЃРѕРє Р»РѕРєР°С†РёР№
     */
    List<LocationEntity> findByCity(String city);

    /**
     * РќР°Р№С‚Рё РІСЃРµ Р»РѕРєР°С†РёРё РїРѕ СЂР°Р№РѕРЅСѓ.
     *
     * @param district СЂР°Р№РѕРЅ
     * @return СЃРїРёСЃРѕРє Р»РѕРєР°С†РёР№
     */
    List<LocationEntity> findByDistrict(String district);

    /**
     * РќР°Р№С‚Рё Р»РѕРєР°С†РёРё РїРѕ СѓСЂРѕРІРЅСЋ РѕРїР°СЃРЅРѕСЃС‚Рё.
     *
     * @param dangerLevel СѓСЂРѕРІРµРЅСЊ РѕРїР°СЃРЅРѕСЃС‚Рё
     * @return СЃРїРёСЃРѕРє Р»РѕРєР°С†РёР№
     */
    List<LocationEntity> findByDangerLevel(LocationEntity.DangerLevel dangerLevel);

    /**
     * РќР°Р№С‚Рё СЃС‚Р°СЂС‚РѕРІСѓСЋ Р»РѕРєР°С†РёСЋ (Downtown).
     *
     * @return СЃС‚Р°СЂС‚РѕРІР°СЏ Р»РѕРєР°С†РёСЏ
     */
    default Optional<LocationEntity> findStartingLocation() {
        return findById("loc-downtown-001");
    }
}

