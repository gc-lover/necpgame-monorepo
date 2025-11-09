package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.VendorInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * VendorInventoryRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРЅРІРµРЅС‚Р°СЂРµРј С‚РѕСЂРіРѕРІС†Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/trading/trading.yaml
 */
@Repository
public interface VendorInventoryRepository extends JpaRepository<VendorInventoryEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІРµСЃСЊ РёРЅРІРµРЅС‚Р°СЂСЊ С‚РѕСЂРіРѕРІС†Р°.
     */
    @Query("SELECT vi FROM VendorInventoryEntity vi WHERE vi.vendorId = :vendorId AND vi.available = true ORDER BY vi.item.name")
    List<VendorInventoryEntity> findByVendorId(String vendorId);

    /**
     * РќР°Р№С‚Рё РєРѕРЅРєСЂРµС‚РЅС‹Р№ РїСЂРµРґРјРµС‚ Сѓ С‚РѕСЂРіРѕРІС†Р°.
     */
    @Query("SELECT vi FROM VendorInventoryEntity vi WHERE vi.vendorId = :vendorId AND vi.itemId = :itemId")
    Optional<VendorInventoryEntity> findByVendorIdAndItemId(String vendorId, String itemId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ РµСЃС‚СЊ Р»Рё РїСЂРµРґРјРµС‚ Сѓ С‚РѕСЂРіРѕРІС†Р°.
     */
    @Query("SELECT COUNT(vi) > 0 FROM VendorInventoryEntity vi WHERE vi.vendorId = :vendorId AND vi.itemId = :itemId AND vi.available = true")
    boolean existsByVendorIdAndItemId(String vendorId, String itemId);
}

