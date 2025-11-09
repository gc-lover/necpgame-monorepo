package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VendorRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С‚РѕСЂРіРѕРІС†Р°РјРё.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/trading/trading.yaml
 */
@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, String> {

    /**
     * РќР°Р№С‚Рё С‚РѕСЂРіРѕРІС†РµРІ РІ Р»РѕРєР°С†РёРё.
     */
    @Query("SELECT v FROM VendorEntity v WHERE v.locationId = :locationId AND v.available = true ORDER BY v.name")
    List<VendorEntity> findByLocationId(String locationId);

    /**
     * РќР°Р№С‚Рё С‚РѕСЂРіРѕРІС†РµРІ РїРѕ С‚РёРїСѓ.
     */
    @Query("SELECT v FROM VendorEntity v WHERE v.vendorType = :type AND v.available = true ORDER BY v.name")
    List<VendorEntity> findByType(String type);

    /**
     * РќР°Р№С‚Рё РґРѕСЃС‚СѓРїРЅС‹С… С‚РѕСЂРіРѕРІС†РµРІ.
     */
    @Query("SELECT v FROM VendorEntity v WHERE v.available = true ORDER BY v.name")
    List<VendorEntity> findAllAvailable();
}

