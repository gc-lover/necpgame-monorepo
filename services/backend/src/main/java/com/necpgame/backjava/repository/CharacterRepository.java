package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїРµСЂСЃРѕРЅР°Р¶Р°РјРё РёРіСЂРѕРєРѕРІ
 */
@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РїРµСЂСЃРѕРЅР°Р¶Рё Р°РєРєР°СѓРЅС‚Р°
     */
    @Query("SELECT c FROM CharacterEntity c " +
           "LEFT JOIN FETCH c.city " +
           "LEFT JOIN FETCH c.faction " +
           "LEFT JOIN FETCH c.appearance " +
           "WHERE c.account.id = :accountId")
    List<CharacterEntity> findAllByAccountId(@Param("accountId") UUID accountId);
    
    /**
     * РќР°Р№С‚Рё РїРµСЂСЃРѕРЅР°Р¶Р° РїРѕ ID СЃ Р·Р°РіСЂСѓР·РєРѕР№ РІСЃРµС… СЃРІСЏР·Р°РЅРЅС‹С… РґР°РЅРЅС‹С…
     */
    @Query("SELECT c FROM CharacterEntity c " +
           "LEFT JOIN FETCH c.account " +
           "LEFT JOIN FETCH c.city " +
           "LEFT JOIN FETCH c.faction " +
           "LEFT JOIN FETCH c.appearance " +
           "WHERE c.id = :id")
    Optional<CharacterEntity> findByIdWithDetails(@Param("id") UUID id);
    
    /**
     * РќР°Р№С‚Рё РїРµСЂСЃРѕРЅР°Р¶Р° РїРѕ ID Рё Р°РєРєР°СѓРЅС‚Сѓ (РґР»СЏ РїСЂРѕРІРµСЂРєРё РІР»Р°РґРµРЅРёСЏ)
     */
    Optional<CharacterEntity> findByIdAndAccountId(UUID id, UUID accountId);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р° СЃ С‚Р°РєРёРј РёРјРµРЅРµРј Сѓ Р°РєРєР°СѓРЅС‚Р°
     */
    boolean existsByNameAndAccountId(String name, UUID accountId);
    
    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ РєРѕР»РёС‡РµСЃС‚РІРѕ РїРµСЂСЃРѕРЅР°Р¶РµР№ Сѓ Р°РєРєР°СѓРЅС‚Р°
     */
    long countByAccountId(UUID accountId);

    Optional<CharacterEntity> findByIdAndAccountIdAndDeletedFalse(UUID id, UUID accountId);

    boolean existsByNameAndAccountIdAndDeletedFalse(String name, UUID accountId);

    long countByAccountIdAndDeletedFalse(UUID accountId);
}

