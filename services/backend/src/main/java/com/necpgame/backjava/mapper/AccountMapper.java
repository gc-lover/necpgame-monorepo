package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.model.Account;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

/**
 * Mapper РґР»СЏ РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ AccountEntity в†” Account DTO
 */
@Component
public class AccountMapper {
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ Entity РІ DTO
     */
    public Account toDto(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Account dto = new Account();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setLastLogin(entity.getLastLogin() != null ? 
            JsonNullable.of(entity.getLastLogin()) : JsonNullable.undefined());
        dto.setIsActive(entity.getIsActive());
        
        return dto;
    }
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ DTO РІ Entity (РґР»СЏ СЃРѕР·РґР°РЅРёСЏ РЅРѕРІРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°)
     */
    public AccountEntity toEntity(Account dto) {
        if (dto == null) {
            return null;
        }
        
        AccountEntity entity = new AccountEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setLastLogin(dto.getLastLogin() != null && dto.getLastLogin().isPresent() ? 
            dto.getLastLogin().get() : null);
        entity.setIsActive(dto.getIsActive());
        
        return entity;
    }
}

