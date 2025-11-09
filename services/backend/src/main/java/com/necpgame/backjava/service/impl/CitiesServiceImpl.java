package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.model.City;
import com.necpgame.backjava.model.GetCities200Response;
import com.necpgame.backjava.repository.CityRepository;
import com.necpgame.backjava.service.CitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для работы с городами.
 * 
 * Источник: API-SWAGGER/api/v1/auth/character-creation-reference-models.yaml
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CitiesServiceImpl implements CitiesService {
    
    private final CityRepository cityRepository;
    
    @Override
    @Transactional(readOnly = true)
    public GetCities200Response getCities(UUID factionId, String region) {
        log.info("Getting cities (factionId: {}, region: {})", factionId, region);
        
        List<CityEntity> entities;
        
        if (factionId != null && region != null) {
            // TODO: Filter by faction AND region
            entities = cityRepository.findAll();
        } else if (factionId != null) {
            // TODO: Filter by faction
            entities = cityRepository.findAll();
        } else if (region != null) {
            entities = cityRepository.findByRegion(region);
        } else {
            entities = cityRepository.findAll();
        }
        
        List<City> cities = new ArrayList<>();
        for (CityEntity entity : entities) {
            City city = new City();
            city.setId(entity.getId());
            city.setName(entity.getName());
            city.setDescription(entity.getDescription());
            city.setRegion(entity.getRegion());
            
            // availableForFactions - convert entities to UUIDs
            List<UUID> factionIds = new ArrayList<>();
            if (entity.getAvailableFactions() != null) {
                entity.getAvailableFactions().forEach(f -> factionIds.add(f.getId()));
            }
            city.setAvailableForFactions(factionIds);
            
            cities.add(city);
        }
        
        GetCities200Response response = new GetCities200Response();
        response.setCities(cities);
        return response;
    }
}

