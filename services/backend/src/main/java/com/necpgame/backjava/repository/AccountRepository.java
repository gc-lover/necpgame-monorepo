package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Р°РєРєР°СѓРЅС‚Р°РјРё РёРіСЂРѕРєРѕРІ
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё Р°РєРєР°СѓРЅС‚ РїРѕ email
     */
    Optional<AccountEntity> findByEmail(String email);
    
    /**
     * РќР°Р№С‚Рё Р°РєРєР°СѓРЅС‚ РїРѕ username
     */
    Optional<AccountEntity> findByUsername(String username);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ Р°РєРєР°СѓРЅС‚Р° РїРѕ email
     */
    boolean existsByEmail(String email);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ Р°РєРєР°СѓРЅС‚Р° РїРѕ username
     */
    boolean existsByUsername(String username);
    
    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅС‹Р№ Р°РєРєР°СѓРЅС‚ РїРѕ email РёР»Рё username
     */
    @Query("SELECT a FROM AccountEntity a WHERE (a.email = :login OR a.username = :login) AND a.isActive = true")
    Optional<AccountEntity> findActiveByEmailOrUsername(@Param("login") String login);
}

