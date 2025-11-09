package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.model.City;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper РґР»СЏ РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ CityEntity в†” City DTO
 */
@Component
public class CityMapper {
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ Entity РІ DTO
     */
    public City toDto(CityEntity entity) {
        if (entity == null) {
            return null;
        }
        
        City dto = new City();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setRegion(entity.getRegion());
        dto.setDescription(entity.getDescription());
        
        // Available for factions (UUID list)
        if (entity.getAvailableFactions() != null) {
            dto.setAvailableForFactions(entity.getAvailableFactions().stream()
                .map(f -> f.getId())
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}

